package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Reward;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.PageRewardRecordRequest;
import com.example.sharablead.request.RewardMeRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-09
 */
public interface RewardService extends IService<Reward> {

    GlobalResponse rewardMe(RewardMeRequest rewardMeRequest);

    GlobalResponse pageRewardRecord(PageRewardRecordRequest pageRewardRecordRequest);
}
