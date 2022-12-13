package com.example.sharablead.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageNotifyDTO {

    private String content;

    private Integer type;

    private Long userId;

}
