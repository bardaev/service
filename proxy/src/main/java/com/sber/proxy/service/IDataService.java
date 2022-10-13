package com.sber.proxy.service;

import com.sber.proxy.entity.Info;
import com.sber.proxy.exception.InfoNotExistException;

public interface IDataService {
    void createOrUpdateInfo(Info info);
    Info getInfoById(Long id) throws InfoNotExistException;
}
