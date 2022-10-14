package com.sber.cache.repository;

import com.sber.cache.entity.Info;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class InfoRepository implements IInfoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Info createOrUpdateInfo(Info info) {
        Info dbInfo = entityManager.find(Info.class, info.getId());
        if (dbInfo == null) {
            entityManager.persist(info);
            return info;
        }
        if (info.getChangeTime().isAfter(dbInfo.getChangeTime())) {
            dbInfo.setValue(info.getValue());
            dbInfo.setChangeTime(info.getChangeTime());
        }
        return dbInfo;
    }

    public Info getInfo(Long id) {
        return entityManager.find(Info.class, id);
    }

}
