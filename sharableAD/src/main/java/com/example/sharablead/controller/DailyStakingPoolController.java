package com.example.sharablead.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.service.CommentService;
import com.example.sharablead.service.DailyStakingPoolService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
@Slf4j
@RestController
@RequestMapping("/daily-staking-pool")
public class DailyStakingPoolController {

    private static final String APISTR = "dailyStakingPool operation";

    @Autowired
    private DailyStakingPoolService dailyStakingPoolService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "getDailyStakingPool", notes = APISTR + "getDailyStakingPool")
    @GetMapping("/getDailyStakingPool")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getDailyStakingPool(HttpServletRequest httpServletRequest, @RequestParam(value = "opusId", required = true, defaultValue = "0") Long opusId){
        String token = httpServletRequest.getHeader("token");
        Long userId = 0L;
        if (StringUtils.isNotBlank(token)){
            try {
                Token token1 = tokenUtil.parseToken(token);
                userId = token1.getUserId();
            }catch (Exception e){
                log.error("pageOpusList abnormal: token is carried but invalid");
            }
        }

        return dailyStakingPoolService.getDailyStakingPool(opusId, LocalDate.now(), userId);
    }

}
