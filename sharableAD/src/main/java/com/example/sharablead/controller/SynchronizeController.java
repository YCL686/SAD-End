package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.SynchronizeService;
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
 * @since 2022-11-23
 */
@RestController
@RequestMapping("/synchronize")
public class SynchronizeController {

    private static final String APISTR = "synchronize operation";

    @Autowired
    private SynchronizeService synchronizeService;

    @ApiOperation(value = "pageSynchronize", notes = APISTR + "pageSynchronize")
    @GetMapping("/pageSynchronize")
    @ApiOperationSupport(order = 1)
    public GlobalResponse pageSynchronize(@RequestParam(required = true, defaultValue = "1") long pageNo, @RequestParam(required = true, defaultValue = "5") long pageSize) {
        return synchronizeService.pageSynchronize(pageNo, pageSize);
    }
}
