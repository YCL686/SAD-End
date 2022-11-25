package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author YCL686
 * @Date 2022/11/4
 */
@Data
public class GetDailyTaskVO {

    private Long taskId;

    private String taskName;

    private Integer taskCount;

    private BigDecimal taskReward;

    //已完成数量
    private Integer taskCounted = 0;

    //是否已完成
    private Boolean finished = false;

    //是否已领取奖励
    private Boolean gotten = false;

}
