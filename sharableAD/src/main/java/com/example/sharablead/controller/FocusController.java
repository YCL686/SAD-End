package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.request.LoginRequest;
import com.example.sharablead.request.OperateFocusRequest;
import com.example.sharablead.service.FocusService;
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
@RequestMapping("/focus")
public class FocusController {

    private static final String APISTR = "focus operation";

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private FocusService focusService;

    @ApiOperation(value = "operateFocus", notes = APISTR + "operateFocus")
    @PostMapping("/operateFocus")
    @ApiOperationSupport(order = 1)
    public GlobalResponse operateFocus(HttpServletRequest httpServletRequest, @RequestBody OperateFocusRequest operateFocusRequest) {
        String token = httpServletRequest.getHeader("token");
        operateFocusRequest.setUserId(tokenUtil.parseToken(token).getUserId());
        return focusService.operateFocus(operateFocusRequest);
    }
}
