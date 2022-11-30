package com.example.sharablead.controller;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.request.LoginRequest;
import com.example.sharablead.service.Web3jService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    Web3jService web3jService;

    @GetMapping("/getSwapPrice")
    public GlobalResponse getSwapPrice() {
        return web3jService.getSwapPrice();
    }
}
