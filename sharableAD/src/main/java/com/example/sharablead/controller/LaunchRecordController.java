package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.AddLaunchRequest;
import com.example.sharablead.request.OperateLikeRequest;
import com.example.sharablead.service.LaunchRecordService;
import com.example.sharablead.service.LikeService;
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
 * @since 2022-11-28
 */
@RestController
@RequestMapping("/launch-record")
public class LaunchRecordController {

    private static final String APISTR = "launch-record operation";

    @Autowired
    private LaunchRecordService launchRecordService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "addLaunch", notes = APISTR + "addLaunch")
    @PostMapping("/addLaunch")
    @ApiOperationSupport(order = 1)
    public GlobalResponse addLaunch(@RequestBody AddLaunchRequest addLaunchRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Token token1 = tokenUtil.parseToken(token);
        addLaunchRequest.setUserId(token1.getUserId());
        return launchRecordService.addLaunch(addLaunchRequest);
    }

}
