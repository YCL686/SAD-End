package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.DailyTaskConfig;
import com.example.sharablead.entity.DailyTaskRecord;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.DailyTaskRecordMapper;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.request.GetDailyTaskRewardRequest;
import com.example.sharablead.service.BalanceEntryService;
import com.example.sharablead.service.DailyTaskConfigService;
import com.example.sharablead.service.DailyTaskRecordService;
import com.example.sharablead.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-04
 */
@Service
public class DailyTaskRecordServiceImpl extends ServiceImpl<DailyTaskRecordMapper, DailyTaskRecord> implements DailyTaskRecordService {

    @Autowired
    private DailyTaskRecordMapper dailyTaskRecordMapper;

    @Autowired
    private DailyTaskConfigService dailyTaskConfigService;

    @Autowired
    private BalanceEntryService balanceEntryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    //TODO add lock a user only could process one request at a time and any exception should rollback
    public GlobalResponse getDailyTaskReward(GetDailyTaskRewardRequest getDailyTaskRewardRequest) {

        Long taskId = getDailyTaskRewardRequest.getTaskId();
        Long userId = getDailyTaskRewardRequest.getUserId();

        if (Objects.isNull(taskId) || taskId == 0L){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid taskId");
        }

        DailyTaskConfig config = dailyTaskConfigService.getById(taskId);

        if (Objects.isNull(config) || DailyTaskConfigStatusEnum.OFFLINE.getCode() == config.getTaskStatus()){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid task config");
        }

        LambdaQueryWrapper<DailyTaskRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DailyTaskRecord::getTaskDate, LocalDate.now());
        lambdaQueryWrapper.eq(DailyTaskRecord::getUserId, userId);
        lambdaQueryWrapper.eq(DailyTaskRecord::getTaskId, taskId);
        if (dailyTaskRecordMapper.selectCount(lambdaQueryWrapper) > 0){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "already gotten");
        }

        BigDecimal rewardAmount = config.getTaskReward();
        DailyTaskRecord record = new DailyTaskRecord();
        record.setId(IDUtil.nextId());
        record.setTaskId(taskId);
        record.setGmtCreated(LocalDateTime.now());
        record.setGmtModified(LocalDateTime.now());
        record.setUserId(userId);
        record.setTaskDate(LocalDate.now());
        record.setTaskReward(rewardAmount);

        ChangeBalanceEntryRequest request = new ChangeBalanceEntryRequest();
        request.setAmount(rewardAmount);
        request.setEntryEvent(AccountEntryEventEnum.DAILY_TASK.getCode());
        request.setEntryType(AccountEntryTypeEnum.IN.getCode());
        request.setUserId(userId);
        request.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());

        if (balanceEntryService.changeBalanceEntry(request)){
            dailyTaskRecordMapper.insert(record);
            //TODO send a message to notify user
            return GlobalResponse.success(true);
        }

        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "get error");
    }
}
