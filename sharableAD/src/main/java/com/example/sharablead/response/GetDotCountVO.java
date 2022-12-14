package com.example.sharablead.response;

import lombok.Data;

@Data
public class GetDotCountVO {

    private Integer unfinishedDailyTaskCount = 0;

    private Integer unreadMessageCount = 0;
}
