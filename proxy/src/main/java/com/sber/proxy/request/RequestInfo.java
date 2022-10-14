package com.sber.proxy.request;

import com.sber.proxy.entity.Info;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.Callable;

public class RequestInfo implements Callable<Info> {

    private final URI url;
    private final RestTemplate restTemplate;

    public RequestInfo(URI url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public Info call() {
        Info result;
        try {
            result = restTemplate.getForObject(url, Info.class);
        } catch (RestClientException e) {
            result = null;
        }
        return result;
    }
}
