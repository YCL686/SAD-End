package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.CronJob;
import com.example.sharablead.enums.CronJobCodeEnum;
import com.example.sharablead.enums.CronJobStatusEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.CronJobMapper;
import com.example.sharablead.service.CronJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.SpecificCronJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-11
 */
@Slf4j
@Service
public class CronJobServiceImpl extends ServiceImpl<CronJobMapper, CronJob> implements CronJobService {

    private static final String cronRegEx = "(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]))|^([0-9]|[0-5][0-9])|^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7])|([1-7])|(\\?)|(\\*)|(([1-7]L)|([1-7]\\#[1-4]))))|(((^([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\* ))((([0-9]|[0-5][0-9])(\\,|\\-|\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\* ))((([0-9]|[01][0-9]|2[0-3])(\\,|\\-|\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\* ))((([0-9]|[0-2][0-9]|3[01])(\\,|\\-|\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\? )|(\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\,|\\-|\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\* ))(([1-7](\\,|\\-|\\/){1}[1-7] )|([1-7] )|(\\? )|(\\* )|(([1-7]L )|([1-7]\\#[1-4]) ))((19[789][0-9]|20[0-9][0-9])\\-(19[789][0-9]|20[0-9][0-9])))";

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public Map<Integer, ScheduledFuture<?>> cronJobMap = new HashMap<>();

    @Autowired
    private SpecificCronJobService specificCronJobService;
    @Autowired
    private CronJobService cronJobService;

    @Override
    public void executeSpecificCronJob(Integer cronJobCode) {
        //refer to CronJobCodeEnum
        long time = System.currentTimeMillis();
        log.info("{} execution start...", CronJobCodeEnum.getName(cronJobCode));
        switch (cronJobCode){
            case 0:
                specificCronJobService.calculateActiveScore();
                log.info("{} execution finished... caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
            case 1:
                specificCronJobService.calculateHotScore();
                log.info("{} execution finished, caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
            case 2:
                specificCronJobService.calculateDailyTaskReward();
                log.info("{} execution finished, caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
            case 3:
                //TODO dailyStaking settlement
                break;
            case 4:
                specificCronJobService.processWithdrawRequest();
                log.info("{} execution finished, caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
            case 5:
                specificCronJobService.synchronizeWallet();
                log.info("{} execution finished, caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
            case 6:
                specificCronJobService.processAdAuctionBusiness();
                log.info("{} execution finished, caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
            case 7:
                specificCronJobService.launchStart();
                log.info("{} execution finished, caused {} ms", CronJobCodeEnum.getName(cronJobCode), System.currentTimeMillis() - time);
                break;
        }
    }

    /**
     * 具体定时任务的处理逻辑
     * @param cronJobCode
     * @return
     */
    @Override
    public Runnable getRunnable(Integer cronJobCode) {
        return new Runnable() {
            @Override
            public void run() {
                log.info(CronJobCodeEnum.getName(cronJobCode) + "---execute automatically---" + LocalDateTime.now());
                executeSpecificCronJob(cronJobCode);
            }
        };
    }

    @Override
    public GlobalResponse getCronJobList() {
        List<CronJob> list = cronJobService.list();
        return GlobalResponse.success(list);
    }

    @Override
    public GlobalResponse startCronJob(Integer cronJobCode) {

        if (cronJobCode == null || "-".equals(CronJobCodeEnum.getName(cronJobCode))){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cronJobCode");
        }

        LambdaQueryWrapper<CronJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CronJob::getCronJobCode, cronJobCode);
        CronJob cronJob = cronJobService.getOne(lambdaQueryWrapper);
        if (Objects.isNull(cronJob)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cronJobCode");
        }

        if (cronJob.getStatus() == CronJobStatusEnum.ONLINE.getCode()){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "already started");
        }

        cronJob.setStatus(CronJobStatusEnum.ONLINE.getCode());
        cronJob.setGmtModified(LocalDateTime.now());
        cronJobService.updateById(cronJob);

        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(getRunnable(cronJobCode), new CronTrigger(cronJob.getCron()));
        cronJobMap.put(cronJobCode, schedule);
        return GlobalResponse.success(true);
    }

    @Override
    public GlobalResponse stopCronJob(Integer cronJobCode) {
        if (cronJobCode == null || "-".equals(CronJobCodeEnum.getName(cronJobCode))){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cronJobCode");
        }

        LambdaQueryWrapper<CronJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CronJob::getCronJobCode, cronJobCode);
        CronJob cronJob = cronJobService.getOne(lambdaQueryWrapper);
        if (Objects.isNull(cronJob)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cronJobCode");
        }

        if (cronJob.getStatus() == CronJobStatusEnum.OFFLINE.getCode()){

            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "already stopped");
        }

        cronJob.setStatus(CronJobStatusEnum.OFFLINE.getCode());
        cronJob.setGmtModified(LocalDateTime.now());
        cronJobService.updateById(cronJob);

        ScheduledFuture<?> scheduledFuture = cronJobMap.get(cronJobCode);
        scheduledFuture.cancel(true);
        cronJobMap.remove(cronJobCode);
        return GlobalResponse.success(true);
    }

    @Override
    public GlobalResponse updateCron(Integer cronJobCode, String cron, String memo) {

        if (StringUtils.isEmpty(memo)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "memo is empty");
        }

        if (cronJobCode == null || "-".equals(CronJobCodeEnum.getName(cronJobCode))){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cronJobCode");
        }

        LambdaQueryWrapper<CronJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CronJob::getCronJobCode, cronJobCode);
        CronJob cronJob = cronJobService.getOne(lambdaQueryWrapper);
        if (Objects.isNull(cronJob)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cronJobCode");
        }

        if (!cron.matches(cronRegEx)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid cron");
        }

        cronJob.setCron(cron);
        cronJob.setMemo(memo);
        cronJob.setGmtModified(LocalDateTime.now());
        cronJobService.updateById(cronJob);

        //如果是开始状态 先删除再添加 停止状态则不处理
        if (cronJobMap.containsKey(cronJobCode)){
            ScheduledFuture<?> scheduledFuture = cronJobMap.get(cronJobCode);
            scheduledFuture.cancel(true);
            cronJobMap.remove(cronJobCode);
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(getRunnable(cronJobCode), new CronTrigger(cron));
            cronJobMap.put(cronJobCode, schedule);
            return GlobalResponse.success(true);
        }

        return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
    }

    /**
     * 初始化定时任务map 从数据库读取所有online状态的定时任务列表
     */
    @PostConstruct
    public void initCronJob() {
        LambdaQueryWrapper<CronJob> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CronJob::getStatus, CronJobStatusEnum.ONLINE.getCode());
        List<CronJob> list = cronJobService.list(lambdaQueryWrapper);
        list.forEach(s -> {
            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(getRunnable(s.getCronJobCode()), new CronTrigger(s.getCron()));
            cronJobMap.put(s.getCronJobCode(), schedule);
            log.info("initCronJob: cronJob init success, cronJobCode = {}, cronJobName = {}, cron = {}", s.getCronJobCode(), s.getCronJobName(), s.getCron());
        });
    }
}
