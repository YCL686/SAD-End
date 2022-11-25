package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.request.BidBuyRequest;
import com.example.sharablead.service.AdAuctionRecordService;
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
@RequestMapping("/ad-auction-record")
public class AdAuctionRecordController {

    private static final String APISTR = "adAuctionRecord operation";

    @Autowired
    private AdAuctionRecordService adAuctionRecordService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "pageBidRecord", notes = APISTR + "pageBidRecord")
    @GetMapping("/pageBidRecord")
    @ApiOperationSupport(order = 1)
    public GlobalResponse pageBidRecord(@RequestParam(required = true, defaultValue = "1")long pageNo, @RequestParam(required = true, defaultValue = "5")long pageSize, @RequestParam(required = true, defaultValue = "0")long adAuctionId) {
        return adAuctionRecordService.pageBidRecord(pageNo, pageSize, adAuctionId);
    }

    @ApiOperation(value = "bidBuy", notes = APISTR + "bidBuy")
    @PostMapping("/bidBuy")
    @ApiOperationSupport(order = 1)
    public GlobalResponse bidBuy(HttpServletRequest httpServletRequest, @RequestBody BidBuyRequest bidBuyRequest) {
        String token = httpServletRequest.getHeader("token");
        bidBuyRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return adAuctionRecordService.bidBuy(bidBuyRequest);
    }

}
