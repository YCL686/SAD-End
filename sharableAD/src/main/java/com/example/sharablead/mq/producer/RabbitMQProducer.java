package com.example.sharablead.mq.producer;

import com.example.sharablead.dto.MessageNotifyDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class RabbitMQProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void produceMessageNotify(MessageNotifyDTO dto) {
        rabbitTemplate.convertAndSend(dto.toString());
    }
}
