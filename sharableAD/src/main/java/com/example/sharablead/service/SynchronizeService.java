package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Synchronize;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-23
 */
public interface SynchronizeService extends IService<Synchronize> {

    GlobalResponse pageSynchronize(long pageNo, long pageSize);
}
