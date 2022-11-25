package com.example.sharablead.service.impl;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.service.ContentService;
import com.example.sharablead.util.SensitiveFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 内容服务
 * 基于DFA算法的敏感词过滤
 * waiting for the implementation of Dr.Ming
 */
@Slf4j
@Service
public class ContentServiceImpl implements ContentService {

    @Override
    public GlobalResponse checkContentSensitive(String content) {
        try {
            SensitiveFilterUtil sensitiveFilter = SensitiveFilterUtil.getInstance();
            String result = sensitiveFilter.replaceSensitiveWord(content, 2, "*");
            return GlobalResponse.success(result);
        }catch (Exception e){
            log.error("checkContentSensitive error: {}", e.getMessage());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), e.getMessage());
        }

    }
}
