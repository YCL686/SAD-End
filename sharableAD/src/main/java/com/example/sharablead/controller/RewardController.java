package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.OperateCollectRequest;
import com.example.sharablead.request.PageRewardRecordRequest;
import com.example.sharablead.request.RewardMeRequest;
import com.example.sharablead.service.CollectService;
import com.example.sharablead.service.RewardService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/reward")
public class RewardController {

    private static final String APISTR = "reward operation";

    @Autowired
    private RewardService rewardService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "rewardMe", notes = APISTR + "rewardMe")
    @PostMapping("/rewardMe")
    @ApiOperationSupport(order = 1)
    public GlobalResponse operateCollect(@RequestBody RewardMeRequest rewardMeRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Token token1 = tokenUtil.parseToken(token);
        rewardMeRequest.setFromUserId(token1.getUserId());
        return rewardService.rewardMe(rewardMeRequest);
    }

    @ApiOperation(value = "pageRewardRecord", notes = APISTR + "pageRewardRecord")
    @GetMapping("/pageRewardRecord")
    @ApiOperationSupport(order = 1)
    public GlobalResponse pageRewardRecord(PageRewardRecordRequest pageRewardRecordRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Token token1 = tokenUtil.parseToken(token);
        pageRewardRecordRequest.setFromUserId(token1.getUserId());
        return rewardService.pageRewardRecord(pageRewardRecordRequest);
    }

}
