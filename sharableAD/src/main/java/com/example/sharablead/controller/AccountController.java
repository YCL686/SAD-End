package com.example.sharablead.controller;


import cn.hutool.http.server.HttpServerRequest;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.request.DepositRequest;
import com.example.sharablead.request.LoginRequest;
import com.example.sharablead.request.WithdrawRequest;
import com.example.sharablead.service.AccountService;
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
 * @since 2022-10-13
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private static final String APISTR = "account operation";

    @Autowired
    private AccountService accountService;

    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 根据用户id获取账户信息 主要是offChainToken余额
     * @return
     */
    @ApiOperation(value = "getAccount", notes = APISTR + "getAccount")
    @GetMapping("/getAccount")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getAccount(HttpServletRequest request) {
        String token = request.getHeader("token");
        return accountService.getAccount(tokenUtil.parseToken(token).getUserId());
    }

    @ApiOperation(value = "deposit", notes = APISTR + "deposit")
    @PostMapping("/deposit")
    @ApiOperationSupport(order = 1)
    public GlobalResponse deposit(HttpServletRequest httpServletRequest, @RequestBody DepositRequest depositRequest) {
        String token = httpServletRequest.getHeader("token");
        depositRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return accountService.deposit(depositRequest);
    }

    @ApiOperation(value = "withdraw", notes = APISTR + "withdraw")
    @PostMapping("/withdraw")
    public GlobalResponse withdraw(HttpServletRequest httpServletRequest, @RequestBody WithdrawRequest withdrawRequest){
        String token = httpServletRequest.getHeader("token");
        withdrawRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return accountService.withdraw(withdrawRequest);
    }

}
