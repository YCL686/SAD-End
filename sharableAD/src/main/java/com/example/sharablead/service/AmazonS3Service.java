package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service {

    GlobalResponse upload(MultipartFile file);

    GlobalResponse uploadAd(MultipartFile file, Long userId);

    GlobalResponse uploadLaunch(MultipartFile file, Long userId);
}
