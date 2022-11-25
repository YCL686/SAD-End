package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.DailyTaskConfig;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-04
 */
public interface DailyTaskConfigService extends IService<DailyTaskConfig> {

    GlobalResponse getDailyTask(Long userId);
}
