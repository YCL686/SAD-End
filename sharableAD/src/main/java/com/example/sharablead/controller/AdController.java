package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.request.EditMyAdRequest;
import com.example.sharablead.request.OperateCollectRequest;
import com.example.sharablead.service.AdService;
import com.example.sharablead.service.CollectService;
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
 * @since 2022-11-17
 */
@RestController
@RequestMapping("/ad")
public class AdController {

    private static final String APISTR = "ad operation";

    @Autowired
    private AdService adService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "getAdList", notes = APISTR + "getAdList")
    @GetMapping("/getAdList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getAdList() {
        return adService.getAdList();
    }

    @ApiOperation(value = "getMyAdList", notes = APISTR + "getMyAdList")
    @GetMapping("/getMyAdList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getMyAdList(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        return adService.getMyAdList(tokenUtil.parseToken(token).getUserId());
    }

    @ApiOperation(value = "editMyAd", notes = APISTR + "editMyAd")
    @PostMapping("/editMyAd")
    @ApiOperationSupport(order = 1)
    public GlobalResponse editMyAd(HttpServletRequest httpServletRequest, @RequestBody EditMyAdRequest editMyAdRequest) {
        String token = httpServletRequest.getHeader("token");
        editMyAdRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return adService.editMyAd(editMyAdRequest);
    }

}
