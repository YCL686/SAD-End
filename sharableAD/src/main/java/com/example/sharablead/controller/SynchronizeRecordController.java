package com.example.sharablead.controller;


import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.SynchronizeRecordService;
import com.example.sharablead.service.Web3jService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-11-23
 */
@RestController
@RequestMapping("/synchronize-record")
public class SynchronizeRecordController {

    private static final String APISTR = "synchronizeRecord operation";

    @Autowired
    private SynchronizeRecordService synchronizeRecordService;

    @ApiOperation(value = "getSynchronizeRecordList", notes = APISTR + "getSynchronizeRecordList")
    @GetMapping("/getSynchronizeRecordList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getSynchronizeRecordList(@RequestParam(required = true, defaultValue = "1") Long synchronizeId) {
        return synchronizeRecordService.getSynchronizeRecordList(synchronizeId);
    }

    @ApiOperation(value = "getSynchronizeInfo", notes = APISTR + "getSynchronizeInfo")
    @GetMapping("/getSynchronizeInfo")
    @ApiOperationSupport(order = 1)
    public GlobalResponse getSynchronizeInfo() {
        return synchronizeRecordService.getSynchronizeInfo();
    }
}
