package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.LaunchService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-28
 */
@RestController
@RequestMapping("/launch")
public class LaunchController {

    private static final String APISTR = "launch operation";

    @Autowired
    private LaunchService launchService;

    @ApiOperation(value = "getLaunchList", notes = APISTR + "getLaunchList")
    @GetMapping("/getLaunchList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getLaunchList() {
        return launchService.getLaunchList();
    }

}
