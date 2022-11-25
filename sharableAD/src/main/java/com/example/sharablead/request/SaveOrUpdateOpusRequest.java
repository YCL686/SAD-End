package com.example.sharablead.request;

import lombok.Data;

@Data
public class SaveOrUpdateOpusRequest {

    private Long id;

    private Long userId;

    private String summary;

    private String text;

    private String content;

    private String title;

    private String resourceUrl;

    private Integer type;

}
