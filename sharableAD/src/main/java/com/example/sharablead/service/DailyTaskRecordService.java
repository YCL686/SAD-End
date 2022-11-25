package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.DailyTaskRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.GetDailyTaskRewardRequest;
import com.example.sharablead.request.OperateFocusRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-04
 */
public interface DailyTaskRecordService extends IService<DailyTaskRecord> {

    GlobalResponse getDailyTaskReward(GetDailyTaskRewardRequest getDailyTaskRewardRequest);
}
