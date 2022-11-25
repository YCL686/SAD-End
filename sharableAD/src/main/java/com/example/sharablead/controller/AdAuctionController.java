package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.AdAuctionService;
import com.example.sharablead.service.AdService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
@RestController
@RequestMapping("/ad-auction")
public class AdAuctionController {
    private static final String APISTR = "adAuction operation";

    @Autowired
    private AdAuctionService adAuctionService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "getAdAuctionList", notes = APISTR + "getAdAuctionList")
    @GetMapping("/getAdAuctionList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getAdAuctionList() {
        return adAuctionService.getAdAuctionList();
    }

    @ApiOperation(value = "getAdAuctionInfo", notes = APISTR + "getAdAuctionInfo")
    @GetMapping("/getAdAuctionInfo")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getAdAuctionInfo() {
        return adAuctionService.getAdAuctionInfo();
    }
}
