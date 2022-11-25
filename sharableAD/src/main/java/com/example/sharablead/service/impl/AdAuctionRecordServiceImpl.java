package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.AdAuctionRecordMapper;
import com.example.sharablead.request.BidBuyRequest;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.response.BidRecordVO;
import com.example.sharablead.response.PageBidRecordResponse;
import com.example.sharablead.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
@Service
@Slf4j
public class AdAuctionRecordServiceImpl extends ServiceImpl<AdAuctionRecordMapper, AdAuctionRecord> implements AdAuctionRecordService {

    @Autowired
    private AdAuctionRecordService adAuctionRecordService;

    @Autowired
    private AdAuctionService adAuctionService;

    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceEntryService balanceEntryService;

    @Value("${ad.auction.minimum_bid_ratio}")
    private BigDecimal minimumBidRatio;

    @Value("${ad.auction.maximum_bid_days}")
    private Integer maximumBidDays;

    @Override
    public GlobalResponse pageBidRecord(long pageNo, long pageSize, long adAuctionId) {
        AdAuction adAuction = adAuctionService.getById(adAuctionId);
        if (Objects.isNull(adAuction)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid adAuctionId");
        }

        LambdaQueryWrapper<AdAuctionRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdAuctionRecord::getAdAuctionId, adAuctionId);

        Page<AdAuctionRecord> page = new Page<>(pageNo, pageSize);
        lambdaQueryWrapper.orderByDesc(AdAuctionRecord::getGmtCreated);
        Page<AdAuctionRecord> result = adAuctionRecordService.page(page, lambdaQueryWrapper);

        PageBidRecordResponse response = new PageBidRecordResponse();
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalPages(result.getPages());
        response.setTotal(result.getTotal());

        List<Long> userIds = result.getRecords().stream().map(AdAuctionRecord::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userService.getUserMap(userIds);

        List<BidRecordVO> data = new ArrayList<>();
        result.getRecords().forEach(adAuctionRecord -> {
            BidRecordVO vo = new BidRecordVO();
            BeanUtils.copyProperties(adAuctionRecord, vo);
            vo.setStatusName(AdAuctionRecordStatusEnum.getName(vo.getStatus()));
            vo.setNickName(userMap.containsKey(vo.getUserId()) ? userMap.get(vo.getUserId()).getNickName() : "-");
            vo.setAuctionTypeName(AuctionTypeEnum.getName(vo.getAuctionType()));
            data.add(vo);
        });

        response.setData(data);

        return GlobalResponse.success(response);
    }

    @Override
    //TODO need lock, the lockObject is the adAuction
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse bidBuy(BidBuyRequest bidBuyRequest) {

        Long adAuctionId = bidBuyRequest.getAdAuctionId();
        AdAuction adAuction = adAuctionService.getById(adAuctionId);
        Long userId = bidBuyRequest.getUserId();
        Integer auctionType = bidBuyRequest.getAuctionType();

        if ("-".equals(AuctionTypeEnum.getName(auctionType))) {
            log.error("bidBuy: invalid auctionType, auctionType = {}", auctionType);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid auctionType");
        }

        if (Objects.isNull(adAuction)) {
            log.error("bidBuy: invalidAdAuctionId, adAuctionId = {}", adAuctionId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid adAuctionId");
        }

        if (adAuction.getStatus() != AdAuctionStatusEnum.ONGOING.getCode() || adAuction.getAuctionEndTime().isBefore(LocalDateTime.now())) {
            log.error("bidBuy: invalid status, adAuctionId = {}, status = {}, adAuctionEndTime = {}, now = {}", adAuctionId, adAuction.getStatus(), adAuction.getAuctionEndTime(), LocalDateTime.now());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid status");
        }

        User user = userService.getById(userId);
        if (Objects.isNull(user) || user.getStatus() == UserStatusEnum.BAN.getCode()) {
            log.error("bidBuy: invalid user or status, userId = {}", userId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user");
        }

        Long adId = adAuction.getAdId();
        Ad ad = adService.getById(adId);

        if (Objects.isNull(ad)) {
            log.error("bidBuy: ad invalid, adId = {}", adId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid ad");
        }

        LambdaQueryWrapper<AdAuctionRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(AdAuctionRecord::getGmtCreated);
        lambdaQueryWrapper.eq(AdAuctionRecord::getAdAuctionId, adAuctionId);
        lambdaQueryWrapper.last("limit 1");
        List<AdAuctionRecord> adAuctionRecords = adAuctionRecordService.list(lambdaQueryWrapper);


        if (bidBuyRequest.getAuctionDays() <= 0 || bidBuyRequest.getAuctionDays() > maximumBidDays) {
            log.error("bidBuy: invalid days, maximumBidDays = {}, auctionDays = {}", maximumBidDays, bidBuyRequest.getAuctionDays());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid days");
        }

        if (Objects.isNull(bidBuyRequest.getAuctionUnitPrice()) || bidBuyRequest.getAuctionUnitPrice().compareTo(ad.getStartPrice()) < 0) {
            log.error("bidBuy: invalid price, bidNowUnitPrice = {}, startPrice = {}", bidBuyRequest.getAuctionUnitPrice(), ad.getStartPrice());
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid price");
        }

        AdAuctionRecord adAuctionRecord = new AdAuctionRecord();
        adAuctionRecord.setId(IDUtil.nextId());
        adAuctionRecord.setUserId(userId);
        adAuctionRecord.setAdAuctionId(adAuctionId);
        adAuctionRecord.setAuctionDays(bidBuyRequest.getAuctionDays());
        adAuctionRecord.setGmtCreated(LocalDateTime.now());
        adAuctionRecord.setGmtModified(LocalDateTime.now());
        adAuctionRecord.setMemo(bidBuyRequest.getMemo());
        adAuctionRecord.setAuctionDate(LocalDate.now());

        if (auctionType == AuctionTypeEnum.BID_NOW.getCode()) {
            adAuctionRecord.setStatus(AdAuctionRecordStatusEnum.ONGOING.getCode());
        } else {
            adAuctionRecord.setStatus(AdAuctionRecordStatusEnum.SUCCESS.getCode());
        }

        adAuctionRecord.setAuctionUnitPrice(bidBuyRequest.getAuctionUnitPrice());
        adAuctionRecord.setAuctionTotalPrice(bidBuyRequest.getAuctionUnitPrice().multiply(BigDecimal.valueOf(bidBuyRequest.getAuctionDays())));
        adAuctionRecord.setAuctionType(auctionType);

        //normal bid
        if (adAuctionRecords.size() == 1) {
            AdAuctionRecord latestAdAuctionRecord = adAuctionRecords.get(0);
            ChangeBalanceEntryRequest changeBalanceEntryRequest = new ChangeBalanceEntryRequest();
            changeBalanceEntryRequest.setEntryEvent(AccountEntryEventEnum.AD_AUCTION.getCode());
            changeBalanceEntryRequest.setEntryType(AccountEntryTypeEnum.IN.getCode());
            changeBalanceEntryRequest.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
            changeBalanceEntryRequest.setUserId(latestAdAuctionRecord.getUserId());
            changeBalanceEntryRequest.setAmount(latestAdAuctionRecord.getAuctionTotalPrice());

            //repeat bid for same user
            if (bidBuyRequest.getUserId().equals(latestAdAuctionRecord.getUserId())) {
                log.error("bidBuy: repeat bid, bidNowUserId = {}, latestUserId = {}", bidBuyRequest.getUserId(), latestAdAuctionRecord.getUserId());
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "repeat bid");
            }

            //invalid bid price
            BigDecimal totalRatio = BigDecimal.ONE.add(minimumBidRatio);
            if (bidBuyRequest.getAuctionUnitPrice().multiply(totalRatio).compareTo(latestAdAuctionRecord.getAuctionUnitPrice()) < 0) {
                log.error("bidBuy: invalid price, minimumBidRatio = {}, auctionPrice = {}", minimumBidRatio, bidBuyRequest.getAuctionUnitPrice());
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid bid price");
            }

            //latestBidRecord refund and newest deduction
            if (balanceEntryService.changeBalanceEntry(changeBalanceEntryRequest)) {
                latestAdAuctionRecord.setStatus(AdAuctionRecordStatusEnum.EXCEED.getCode());
                latestAdAuctionRecord.setGmtModified(LocalDateTime.now());
                adAuctionRecordService.updateById(latestAdAuctionRecord);
                //TODO send a message
                ChangeBalanceEntryRequest changeBalanceEntryRequest1 = new ChangeBalanceEntryRequest();
                changeBalanceEntryRequest1.setAmount(bidBuyRequest.getAuctionUnitPrice().multiply(BigDecimal.valueOf(bidBuyRequest.getAuctionDays())));
                changeBalanceEntryRequest1.setEntryType(AccountEntryTypeEnum.OUT.getCode());
                changeBalanceEntryRequest1.setEntryEvent(AccountEntryEventEnum.AD_AUCTION.getCode());
                changeBalanceEntryRequest1.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
                changeBalanceEntryRequest1.setUserId(bidBuyRequest.getUserId());
                if (balanceEntryService.changeBalanceEntry(changeBalanceEntryRequest1)) {
                    adAuctionRecordService.save(adAuctionRecord);
                } else {
                    //rollback manually
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
                }
            } else {
                //rollback manually
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
            }
        } else { //first bid
            ChangeBalanceEntryRequest changeBalanceEntryRequest2 = new ChangeBalanceEntryRequest();
            changeBalanceEntryRequest2.setAmount(bidBuyRequest.getAuctionUnitPrice().multiply(BigDecimal.valueOf(bidBuyRequest.getAuctionDays())));
            changeBalanceEntryRequest2.setEntryType(AccountEntryTypeEnum.OUT.getCode());
            changeBalanceEntryRequest2.setEntryEvent(AccountEntryEventEnum.AD_AUCTION.getCode());
            changeBalanceEntryRequest2.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
            changeBalanceEntryRequest2.setUserId(bidBuyRequest.getUserId());
            if (balanceEntryService.changeBalanceEntry(changeBalanceEntryRequest2)) {
                adAuctionRecordService.save(adAuctionRecord);
            } else {
                //rollback manually
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
            }
        }

        if (auctionType == AuctionTypeEnum.BUY_IT_NOW.getCode()) {
            adAuction.setStatus(AdAuctionStatusEnum.SUCCESS.getCode());
            adAuction.setDealPrice(bidBuyRequest.getAuctionUnitPrice());
            ad.setEditCount(3);
            ad.setUserId(userId);
            ad.setGmtModified(LocalDateTime.now());
            ad.setNextAuctionTime(LocalDate.now().plusDays(bidBuyRequest.getAuctionDays()).atTime(0,0,0,0));
            adService.updateById(ad);
        }
        adAuction.setBidPrice(bidBuyRequest.getAuctionUnitPrice());
        adAuction.setGmtModified(LocalDateTime.now());

        adAuctionService.updateById(adAuction);

        return GlobalResponse.success(true);
    }
}
