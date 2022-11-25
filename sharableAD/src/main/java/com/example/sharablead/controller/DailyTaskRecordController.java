package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.request.GetDailyTaskRewardRequest;
import com.example.sharablead.request.OperateFocusRequest;
import com.example.sharablead.service.DailyTaskRecordService;
import com.example.sharablead.service.FocusService;
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
 * @since 2022-11-04
 */
@RestController
@RequestMapping("/daily-task-record")
public class DailyTaskRecordController {

    private static final String APISTR = "dailyTaskRecord operation";

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private DailyTaskRecordService dailyTaskRecordService;

    @ApiOperation(value = "operateFocus", notes = APISTR + "operateFocus")
    @PostMapping("/getDailyTaskReward")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getDailyTaskReward(HttpServletRequest httpServletRequest, @RequestBody GetDailyTaskRewardRequest getDailyTaskRewardRequest) {
        String token = httpServletRequest.getHeader("token");
        getDailyTaskRewardRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return dailyTaskRecordService.getDailyTaskReward(getDailyTaskRewardRequest);
    }

}
