package com.example.sharablead.controller;


import cn.hutool.http.server.HttpServerRequest;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.AccountEntryService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/account-entry")
public class AccountEntryController {

    private static final String APISTR = "accountEntry operation";

    @Autowired
    private AccountEntryService accountEntryService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "pageAccountEntry", notes = APISTR + "pageAccountEntry")
    @GetMapping("/pageAccountEntry")
    @ApiOperationSupport(order = 1)
    public GlobalResponse pageAccountEntry(HttpServletRequest request, @RequestParam(value = "pageSize", required=true, defaultValue = "5") long pageSize, @RequestParam(value = "pageNo", required=true, defaultValue = "1") long pageNo) {
        String token = request.getHeader("token");
        Long userId = tokenUtil.parseToken(token).getUserId();
        return accountEntryService.pageAccountEntry(pageSize, pageNo, userId);
    }

}
