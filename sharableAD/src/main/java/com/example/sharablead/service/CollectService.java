package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Collect;
import com.example.sharablead.request.OperateCollectRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-20
 */
public interface CollectService extends IService<Collect> {

    GlobalResponse operateCollect(OperateCollectRequest operateCollectRequest);
}
