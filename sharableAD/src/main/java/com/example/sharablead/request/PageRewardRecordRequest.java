package com.example.sharablead.request;

import lombok.Data;

@Data
public class PageRewardRecordRequest {

    private Long pageNo;

    private Long pageSize;

    private Long toUserId;

    private Long fromUserId;
}
