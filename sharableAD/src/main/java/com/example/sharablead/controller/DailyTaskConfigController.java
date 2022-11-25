package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.DailyTaskConfigService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-04
 */
@Slf4j
@RestController
@RequestMapping("/daily-task-config")
public class DailyTaskConfigController {

    private static final String APISTR = "dailyTaskConfig operation";

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private DailyTaskConfigService dailyTaskConfigService;

    @ApiOperation(value = "getDailyTask", notes = APISTR + "getDailyTask")
    @GetMapping("/getDailyTask")
    @ApiOperationSupport(order = 1)
    public GlobalResponse operateFocus(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Long userId = 0L;
        try {
            userId = tokenUtil.parseToken(token).getUserId();
        }catch (Exception e){
            log.info("getDailyTask: not logged");
        }
        return dailyTaskConfigService.getDailyTask(userId);
    }

}
