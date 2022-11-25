package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.SystemConfig;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.SystemConfigMapper;
import com.example.sharablead.service.SystemConfigService;
import com.example.sharablead.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemConfigServiceImpl extends ServiceImpl<SystemConfigMapper, SystemConfig> implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    @Autowired
    private RedisUtil redisUtil;

    public static final String KEY = "system_config";


    @Override
    public Map<String, String> initConfigMap() {
        List<SystemConfig> configs = systemConfigMapper.selectList(null);
        Map<String, String> resMap = new HashMap<>();
        configs.forEach(item->{
            resMap.put(item.getName(), item.getValue());
        });
        return resMap;
    }

    /*
     * @将数据库查询出来的数据存入map和redis
     */
    @PostConstruct
    public void loadInRedis() {
        Map<String, String> config = initConfigMap();
        config.forEach((key, value) -> {
            redisUtil.hmSet(KEY, key, value);
        });
    }

    @Override
    public String getConfigValue(String configName) {
        // 缓存中是否有该配置
        if (redisUtil.hmHasKey(KEY, configName)){
            return redisUtil.hmGet(KEY, configName);
        }else {
            // 重新load一次
            loadInRedis();
            return redisUtil.hmGet(KEY, configName);
        }
    }

    @Override
    public GlobalResponse modify(String hashKey, String value) {
        if (Objects.equals(hashKey, "") || Objects.equals(value, "")) {
            log.error( "hashKey或者value不能为空" );
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "hashKey或者value为空");
        }
        // 更新到mysql数据库
        LambdaQueryWrapper<SystemConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SystemConfig::getName, hashKey);
        List<SystemConfig> configs = systemConfigMapper.selectList(lambdaQueryWrapper);
        SystemConfig config;
        if(configs.size() == 1){
            config = configs.get(0);
        }else {
            log.error(hashKey + "在配置表中有重名" );
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "有同名配置项");
        }
        config.setName(hashKey);
        config.setValue(value);
        systemConfigMapper.updateById(config);

        // 更新到redis
        redisUtil.hmSet(KEY, hashKey, value);

        return GlobalResponse.success();
    }
}
