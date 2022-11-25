package com.example.sharablead.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.service.CronJobService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-11
 */
@RestController
@RequestMapping("/cron-job")
public class CronJobController {

    private static final String APISTR = "cronJob operation";

    @Autowired
    private CronJobService cronJobService;

    @Autowired
    private TokenUtil tokenUtil;

    //获取cronJob列表
    @ApiOperation(value = "getCronJobList", notes = APISTR + "getCronJobList")
    @GetMapping("/getCronJobList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getCronJobList(){
        return cronJobService.getCronJobList();
    }

    //设置某一定时任务为online
    @ApiOperation(value = "startCronJob", notes = APISTR + "startCronJob")
    @PostMapping("/startCronJob")
    @ApiOperationSupport(order = 1)
    public GlobalResponse startCronJob(Integer cronJobCode){
        return cronJobService.startCronJob(cronJobCode);
    }

    //设置某一定时任务为offline
    @ApiOperation(value = "stopCronJob", notes = APISTR + "stopCronJob")
    @PostMapping("/stopCronJob")
    @ApiOperationSupport(order = 1)
    public GlobalResponse stopCronJob(Integer cronJobCode){
        return cronJobService.stopCronJob(cronJobCode);
    }

    //手动触发一次某一定时任务
    @ApiOperation(value = "executeCronJob", notes = APISTR + "executeCronJob")
    @PostMapping("/executeCronJob")
    @ApiOperationSupport(order = 1)
    public GlobalResponse executeCronJob(Integer cronJobCode){
        cronJobService.executeSpecificCronJob(cronJobCode);
        return GlobalResponse.success(true);
    }
    //更新cron表达式
    @ApiOperation(value = "updateCron", notes = APISTR + "updateCron")
    @PostMapping("/updateCron")
    @ApiOperationSupport(order = 1)
    public GlobalResponse updateCron(Integer cronJobCode, String cron, String memo){
        return cronJobService.updateCron(cronJobCode, cron, memo);
    }

}
