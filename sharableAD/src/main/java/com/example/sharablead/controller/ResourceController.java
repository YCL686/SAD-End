package com.example.sharablead.controller;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.AmazonS3Service;
import com.example.sharablead.service.PinataService;
import com.example.sharablead.util.TokenUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/resource")
public class ResourceController {

    private static final String APISTR = "resource operation";

    @Autowired
    private AmazonS3Service amazonS3Service;

    @Resource
    private PinataService pinataService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "upload", notes = APISTR + "upload")
    @PostMapping(value = "/upload")
    public GlobalResponse upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return amazonS3Service.upload(file);
    }

    @ApiOperation(value = "uploadAd", notes = APISTR + "uploadAd")
    @PostMapping(value = "/uploadAd")
    public GlobalResponse uploadAd(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = request.getHeader("token");
        return amazonS3Service.uploadAd(file, tokenUtil.parseToken(token).getUserId());
    }

    @ApiOperation(value = "uploadLaunch", notes = APISTR + "uploadLaunch")
    @PostMapping(value = "/uploadLaunch")
    public GlobalResponse uploadLaunch(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = request.getHeader("token");
        return amazonS3Service.uploadLaunch(file, tokenUtil.parseToken(token).getUserId());
    }

    @ApiOperation(value = "uploadToPinata", notes = APISTR + "uploadToPinata")
    @PostMapping(value = "/uploadToPinata")
    public GlobalResponse uploadToPinata(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String token = request.getHeader("token");
        return pinataService.uploadToPinata(file, tokenUtil.parseToken(token).getUserId());
    }
}
