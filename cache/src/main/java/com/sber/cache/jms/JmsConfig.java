package com.sber.cache.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JmsConfig {

    @Value("${queue}")
    private String queue;

    @Bean
    public Jackson2JsonMessageConverter getConverter(Jackson2ObjectMapperBuilder objectMapperBuilder) {
        ObjectMapper objectMapper = objectMapperBuilder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Declarables fanoutBindings() {
        Queue fanoutQueue = new Queue(queue, false, true, true);
        FanoutExchange fanoutExchange = new FanoutExchange("sber", false, true);

        return new Declarables(
                fanoutQueue,
                fanoutExchange,
                BindingBuilder.bind(fanoutQueue).to(fanoutExchange)
        );
    }
}
