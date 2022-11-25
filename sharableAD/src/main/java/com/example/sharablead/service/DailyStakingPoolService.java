package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.DailyStakingPool;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
public interface DailyStakingPoolService extends IService<DailyStakingPool> {

    GlobalResponse getDailyStakingPool(Long opusId, LocalDate stakingDate, Long userId);
}
