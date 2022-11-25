package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Opus;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.SaveOrUpdateOpusRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-10
 */
public interface OpusService extends IService<Opus> {

    GlobalResponse pageOpusList(long pageSize, long pageNo, int orderType, Long userId);

    GlobalResponse saveOrUpdateOpus(SaveOrUpdateOpusRequest saveOrUpdateOpusRequest);

    GlobalResponse getOpusById(Long opusId, Long userId);

    GlobalResponse pageProfileOpusList(Boolean self, Integer status, long pageNo, long pageSize, Long userId);

    GlobalResponse getOpusByIdForPublish(Long opusId, Long userId);
}
