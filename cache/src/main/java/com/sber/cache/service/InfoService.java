package com.sber.cache.service;

import com.sber.cache.entity.Info;
import com.sber.cache.repository.IInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService implements IInfoService {

    private final IInfoRepository infoRepository;

    @Autowired
    public InfoService(IInfoRepository infoRepository) {
        this.infoRepository = infoRepository;
    }

    @Override
    public Info createOrUpdateInfo(Info info) {
        return infoRepository.createOrUpdateInfo(info);
    }

    @Override
    public Info getInfo(Long id) {
        return infoRepository.getInfo(id);
    }
}
