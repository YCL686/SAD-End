package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PinataService {
    GlobalResponse uploadToPinata(MultipartFile file, Long userId);
}
