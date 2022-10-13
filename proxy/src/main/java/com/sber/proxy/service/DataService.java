package com.sber.proxy.service;

import com.sber.proxy.entity.Info;
import com.sber.proxy.exception.InfoNotExistException;
import com.sber.proxy.jms.OrderMessagingService;
import com.sber.proxy.request.RequestInfo;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class DataService implements IDataService {

    private final static Logger logger = LoggerFactory.getLogger(DataService.class);

    private final RestTemplate restTemplate;
    private final OrderMessagingService messagingService;
    private final ExecutorService executorService;

    @Value("${db.address}")
    private String address;

    @Value("${db.ports}")
    List<Integer> ports;

    @Autowired
    public DataService(RestTemplate restTemplate, OrderMessagingService messagingService, ExecutorService executorService) {
        this.restTemplate = restTemplate;
        this.messagingService = messagingService;
        this.executorService = executorService;
    }

    @Override
    public void createOrUpdateInfo(Info info) {
        info.setChangeTime(LocalDateTime.now());
        messagingService.sendOrder(info);
    }

    @Override
    public Info getInfoById(Long id) {
        List<Future<Info>> requests = new ArrayList<>();
        List<Info> results = new ArrayList<>();
        for (int i = 0; i < ports.size(); i++) {
            requests.add(executorService.submit(new RequestInfo(getAddress(id, ports.get(i)), restTemplate)));
        }
        try {
            for (int i = 0; i < requests.size(); i++) {
                results.add(requests.get(i).get(5000, TimeUnit.MILLISECONDS));
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.warn(e.getMessage());
        }
        results = results
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Info resultInfo = resolveVersionInfo(results);
        if (resultInfo != null) {
            return resultInfo;
        }
        throw new InfoNotExistException("Info class not exist");
    }

    private URI getAddress(Long id, Integer port) {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme("http");
        uriBuilder.setHost(address);
        uriBuilder.setPort(port);
        uriBuilder.setPath("cache/" + id);
        try {
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private Info resolveVersionInfo(List<Info> results) {
        Info minChangeDateInfo = null;
        Info maxChangeDateInfo;

        if (results.size() == 1) {
            return results.get(0);
        } else if (results.size() > 1) {
            minChangeDateInfo = results.get(0);
            maxChangeDateInfo = results.get(0);

            for (int i = 1; i < results.size(); i++) {
                if (results.get(i).getChangeTime().isAfter(results.get(i - 1).getChangeTime()) &&
                results.get(i).getChangeTime().isAfter(minChangeDateInfo.getChangeTime())) {
                    maxChangeDateInfo = results.get(i);
                } else if (results.get(i).getChangeTime().isBefore(results.get(i - 1).getChangeTime()) &&
                results.get(i).getChangeTime().isBefore(maxChangeDateInfo.getChangeTime())) {
                    minChangeDateInfo = results.get(i);
                }
            }

            if (maxChangeDateInfo.getChangeTime().isAfter(minChangeDateInfo.getChangeTime())) {
                createOrUpdateInfo(maxChangeDateInfo);
                return maxChangeDateInfo;
            } else if (maxChangeDateInfo.getChangeTime().isBefore(minChangeDateInfo.getChangeTime())) {
                createOrUpdateInfo(minChangeDateInfo);
                return minChangeDateInfo;
            }
        }

        return minChangeDateInfo;
    }

}
