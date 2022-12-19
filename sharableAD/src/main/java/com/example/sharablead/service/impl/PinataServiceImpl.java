package com.example.sharablead.service.impl;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.service.PinataService;
import com.example.sharablead.util.SignCheckUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class PinataServiceImpl implements PinataService {

    @Value("${web3.pinata.app-key}")
    private String pinataAppKey;

    @Value("${web3.pinata.secret-key}")
    private String pinataSecretKey;

    @Value("${web3.pinata.upload-base-url}")
    private String uploadBaseUrl;

    @Resource
    SignCheckUtil signCheckUtil;

    @Override
    public GlobalResponse uploadToPinata(MultipartFile file, Long userId) {
        //TODO userId check is allowed


        return null;
    }
}
