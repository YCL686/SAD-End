package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.AddCommentRequest;
import com.example.sharablead.request.OperateCollectRequest;
import com.example.sharablead.service.CollectService;
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
@RequestMapping("/collect")
public class CollectController {

    private static final String APISTR = "collect operation";

    @Autowired
    private CollectService collectService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "operateCollect", notes = APISTR + "operateCollect")
    @PostMapping("/operateCollect")
    @ApiOperationSupport(order = 1)
    public GlobalResponse operateCollect(@RequestBody OperateCollectRequest operateCollectRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Token token1 = tokenUtil.parseToken(token);
        operateCollectRequest.setUserId(token1.getUserId());
        return collectService.operateCollect(operateCollectRequest);
    }

}
