package com.sber.proxy.jms;

import com.sber.proxy.entity.Info;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMessagingService implements OrderMessagingService{

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendOrder(Info info) {
        rabbitTemplate.convertAndSend("sber", "", info);
    }
}
