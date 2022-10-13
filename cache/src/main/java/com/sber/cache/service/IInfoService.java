package com.sber.cache.service;


import com.sber.cache.entity.Info;

public interface IInfoService {
    Info createOrUpdateInfo(Info info);
    Info getInfo(Long id);
}
