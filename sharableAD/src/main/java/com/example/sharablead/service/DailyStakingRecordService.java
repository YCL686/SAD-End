package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.DailyStakingRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.StakeDailyStakingRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
public interface DailyStakingRecordService extends IService<DailyStakingRecord> {

    GlobalResponse stakeDailyStaking(StakeDailyStakingRequest stakeDailyStakingRequest);
}
