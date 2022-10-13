package com.sber.proxy.jms;

import com.sber.proxy.entity.Info;

public interface OrderMessagingService {
    public void sendOrder(Info info);
}
