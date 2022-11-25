package com.example.sharablead.request;

import lombok.Data;

@Data
public class OperateLikeRequest {

    private Long userId;

    private Long likedId;

    private Integer likedType;
}
