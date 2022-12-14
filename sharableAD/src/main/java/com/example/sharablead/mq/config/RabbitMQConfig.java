package com.example.sharablead.mq.config;

import com.example.sharablead.constant.MQConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue firstQueue() {
        return new Queue(MQConstant.MESSAGE_NOTIFY_QUEUE, true, false, false);
    }
}

