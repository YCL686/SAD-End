package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.OperateLikeRequest;
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
 * @since 2022-10-20
 */
@RestController
@RequestMapping("/like")
public class LikeController {

    private static final String APISTR = "like operation";

    @Autowired
    private LikeService likeService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "operateLike", notes = APISTR + "operateLike")
    @PostMapping("/operateLike")
    @ApiOperationSupport(order = 1)
    public GlobalResponse operateCollect(@RequestBody OperateLikeRequest operateLikeRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Token token1 = tokenUtil.parseToken(token);
        operateLikeRequest.setUserId(token1.getUserId());
        return likeService.operateLike(operateLikeRequest);
    }

}
