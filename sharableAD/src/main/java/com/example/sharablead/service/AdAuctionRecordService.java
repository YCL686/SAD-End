package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.AdAuctionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.request.BidBuyRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
public interface AdAuctionRecordService extends IService<AdAuctionRecord> {

    GlobalResponse pageBidRecord(long pageNo, long pageSize, long adAuctionId);

    GlobalResponse bidBuy(BidBuyRequest bidBuyRequest);
}
