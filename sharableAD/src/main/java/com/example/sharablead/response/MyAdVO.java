package com.example.sharablead.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MyAdVO {

    private Long id;

    private Integer adIndex;

    private String label;

    private String name;

    private String resourceUrl;

    private String link;

    private Long userId;

    private Integer editCount;

    private LocalDateTime nextAuctionTime;
}
