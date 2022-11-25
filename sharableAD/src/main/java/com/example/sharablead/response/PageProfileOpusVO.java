package com.example.sharablead.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PageProfileOpusVO {

    private Long id;

    private Long userId;

    private String title;

    private String summary;

    private List<String> resourceUrls;

    private Integer minted;

    private Integer status;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;
}
