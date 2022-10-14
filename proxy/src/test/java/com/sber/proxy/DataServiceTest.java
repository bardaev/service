package com.sber.proxy;

import com.sber.proxy.entity.Info;
import com.sber.proxy.service.DataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DataServiceTest {
    @Autowired
    private DataService dataService;

    @Test
    public void resolveSameVersion() {
        LocalDateTime time = LocalDateTime.now();
        Info info1 = new Info(1L, "test", time);
        Info info2 = new Info(1L, "test", time);

        List<Info> infoList = new ArrayList<>();
        infoList.add(info1);
        infoList.add(info2);

        Info result = dataService.resolveVersionInfo(infoList);

        assertEquals(1L, result.getId());
        assertEquals("test", result.getValue());
        assertEquals(time, result.getChangeTime());
    }

    @Test
    public void resolveDifferentVersion() {
        LocalDateTime time1 = LocalDateTime.now();
        LocalDateTime time2 = LocalDateTime.now();
        Info info1 = new Info(1L, "test", time1);
        Info info2 = new Info(1L, "test1", time2);

        List<Info> infoList = new ArrayList<>();
        infoList.add(info1);
        infoList.add(info2);

        Info result = dataService.resolveVersionInfo(infoList);

        assertEquals("test1", result.getValue());
    }
}
