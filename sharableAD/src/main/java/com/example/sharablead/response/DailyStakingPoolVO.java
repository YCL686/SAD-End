package com.example.sharablead.response;

import com.example.sharablead.enums.DailyStakingPoolStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DailyStakingPoolVO {

    private Long deadLine;

    private BigDecimal offChainToken = BigDecimal.ZERO;
    
    //是否可质押 根据status判断以及是否有质押记录
    private Boolean stakable = true;

    private Integer status = DailyStakingPoolStatusEnum.STAKABLE.getCode();

    private String statusName = DailyStakingPoolStatusEnum.STAKABLE.getName();

    //当前opus质押量
    private BigDecimal stakingAmount = BigDecimal.ZERO;

    //当日所有opus质押量
    private BigDecimal totalAmount = BigDecimal.ZERO;

    //当前opus排名
    private Long currentRank = 0L;

    //当前opus热度
    private BigDecimal hotScore = BigDecimal.ZERO;

    //当前opus的用户质押记录展示
    private List<DailyStakingRecordVO> list = new ArrayList<>();

    private Integer stakedNum = 0;

    private Integer totalNum = 10;

}
