package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LaunchVO {

    private Integer launchIndex;

    private LocalDateTime launchMoment;

    private Boolean launchable = false;

    private String launchTime;

    private Integer launchCount;

    private BigDecimal launchPrice;

    private List<LauncherVO> data;

    private Long launchId;

}
