package com.example.sharablead.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.SystemConfig;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 铭帅
 * @since 2022-11-11
 */
public interface SystemConfigService extends IService<SystemConfig> {

    Map<String, String> initConfigMap();

    String getConfigValue(String configName);

    GlobalResponse modify(String hashKey, String value);

}
