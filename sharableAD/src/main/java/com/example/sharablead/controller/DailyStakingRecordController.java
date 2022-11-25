package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.OperateLikeRequest;
import com.example.sharablead.request.StakeDailyStakingRequest;
import com.example.sharablead.service.DailyStakingRecordService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
@RestController
@RequestMapping("/daily-staking-record")
public class DailyStakingRecordController {

    private static final String APISTR = "dailyStakingRecord operation";

    @Autowired
    private DailyStakingRecordService dailyStakingRecordService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "stakeDailyStaking", notes = APISTR + "stakeDailyStaking")
    @PostMapping("/stakeDailyStaking")
    @ApiOperationSupport(order = 1)
    public GlobalResponse operateCollect(@RequestBody StakeDailyStakingRequest stakeDailyStakingRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        stakeDailyStakingRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return dailyStakingRecordService.stakeDailyStaking(stakeDailyStakingRequest);
    }

}
