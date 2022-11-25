package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.AdAuction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
public interface AdAuctionService extends IService<AdAuction> {

    GlobalResponse getAdAuctionList();

    GlobalResponse getAdAuctionInfo();
}
