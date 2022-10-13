package com.sber.cache.repository;

import com.sber.cache.entity.Info;

public interface IInfoRepository {
    Info createOrUpdateInfo(Info info);
    Info getInfo(Long id);
}
