package com.example.sharablead.request;

import lombok.Data;

/**
 * @Author YCL686
 * @Date 2022/11/5
 */
@Data
public class GetDailyTaskRewardRequest {

    private Long taskId;

    private Long userId;
}
