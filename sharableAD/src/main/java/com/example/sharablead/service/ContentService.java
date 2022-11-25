package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;

import java.io.IOException;

public interface ContentService {

    GlobalResponse checkContentSensitive(String content);

}
