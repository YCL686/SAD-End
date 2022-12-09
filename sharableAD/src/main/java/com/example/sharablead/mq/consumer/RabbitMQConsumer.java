package com.example.sharablead.mq.consumer;

import com.example.sharablead.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQConsumer {

    @RabbitHandler
    @RabbitListener(queues = {MQConstant.MESSAGE_NOTIFY_QUEUE})
    public void consumeMessage(String msg){
        log.info(msg);
    }
}
