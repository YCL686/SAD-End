package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.CronJob;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-11
 */
public interface CronJobService extends IService<CronJob> {

    void executeSpecificCronJob(Integer cronJobCode);

    Runnable getRunnable(Integer cronJobCode);

    GlobalResponse getCronJobList();

    GlobalResponse startCronJob(Integer cronJobCode);

    GlobalResponse stopCronJob(Integer cronJobCode);

    GlobalResponse updateCron(Integer cronJobCode, String cron, String memo);
}
