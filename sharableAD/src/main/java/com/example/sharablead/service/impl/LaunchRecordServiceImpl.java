package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Launch;
import com.example.sharablead.entity.LaunchRecord;
import com.example.sharablead.enums.AccountEntryEventEnum;
import com.example.sharablead.enums.AccountEntryStatusEnum;
import com.example.sharablead.enums.AccountEntryTypeEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.LaunchRecordMapper;
import com.example.sharablead.request.AddLaunchRequest;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.service.BalanceEntryService;
import com.example.sharablead.service.LaunchRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.LaunchService;
import com.example.sharablead.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-28
 */
@Service
//TODO addLock
@Transactional(rollbackFor = Exception.class)
public class LaunchRecordServiceImpl extends ServiceImpl<LaunchRecordMapper, LaunchRecord> implements LaunchRecordService {

    @Autowired
    private LaunchRecordService launchRecordService;

    @Autowired
    private LaunchService launchService;

    @Autowired
    private BalanceEntryService balanceEntryService;

    @Override
    public GlobalResponse addLaunch(AddLaunchRequest addLaunchRequest) {

        Long launchId = addLaunchRequest.getLaunchId();
        Long userId = addLaunchRequest.getUserId();

        Launch launch = launchService.getById(launchId);
        int index = LocalDateTime.now().getHour();

        if (Objects.isNull(launch)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid launch");
        }

        if (index > launch.getLaunchIndex()){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid index");
        }
        int launchCount = launch.getLaunchCount();
        LambdaQueryWrapper<LaunchRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(LaunchRecord::getLaunchId, launchId);

        if (launchRecordService.count(lambdaQueryWrapper) >= launchCount){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "reach count");
        }

        lambdaQueryWrapper.eq(LaunchRecord::getUserId, userId);
        if (launchRecordService.count(lambdaQueryWrapper) > 0){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "repeat launch");
        }

        ChangeBalanceEntryRequest request = new ChangeBalanceEntryRequest();
        request.setEntryEvent(AccountEntryEventEnum.LAUNCH.getCode());
        request.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
        request.setAmount(launch.getLaunchPrice());
        request.setUserId(userId);
        request.setEntryType(AccountEntryTypeEnum.OUT.getCode());
        if (!balanceEntryService.changeBalanceEntry(request)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "add error");
        }

        LaunchRecord record = new LaunchRecord();
        record.setId(IDUtil.nextId());
        record.setLaunchId(launchId);
        record.setLaunchDate(LocalDate.now());
        record.setUserId(userId);
        record.setLaunchLink(addLaunchRequest.getLaunchLink());
        record.setLaunchPrice(launch.getLaunchPrice());
        record.setLaunchDescription(addLaunchRequest.getLaunchDescription());
        record.setLaunchTitle(addLaunchRequest.getLaunchTitle());
        record.setLaunchUrl(addLaunchRequest.getLaunchUrl());
        record.setGmtCreated(LocalDateTime.now());
        record.setGmtModified(LocalDateTime.now());

        launchRecordService.save(record);

        return GlobalResponse.success(true);
    }
}
