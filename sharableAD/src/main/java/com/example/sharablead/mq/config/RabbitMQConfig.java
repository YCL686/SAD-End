package com.example.sharablead.mq.config;

import com.example.sharablead.constant.MQConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConfig {

    @Bean
    public Queue firstQueue(){
        return new Queue(MQConstant.MESSAGE_NOTIFY_QUEUE,true,false,false);
    }
}
