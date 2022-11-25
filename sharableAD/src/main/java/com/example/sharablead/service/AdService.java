package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Ad;
import com.example.sharablead.request.EditMyAdRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
public interface AdService extends IService<Ad> {

    GlobalResponse getAdList();

    GlobalResponse getMyAdList(Long userId);

    GlobalResponse editMyAd(EditMyAdRequest editMyAdRequest);
}
