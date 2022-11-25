package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.AdAuctionRecordStatusEnum;
import com.example.sharablead.enums.AdAuctionStatusEnum;
import com.example.sharablead.enums.AdStatusEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.AdAuctionMapper;
import com.example.sharablead.response.AdAuctionInfoVO;
import com.example.sharablead.response.AdAuctionVO;
import com.example.sharablead.service.AdAuctionRecordService;
import com.example.sharablead.service.AdAuctionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.AdService;
import com.example.sharablead.service.UserService;
import com.example.sharablead.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
@Service
public class AdAuctionServiceImpl extends ServiceImpl<AdAuctionMapper, AdAuction> implements AdAuctionService {

    @Value("${ad.auction.feed_back_ratio}")
    private BigDecimal feedbackRatio;

    @Value("${ad.auction.on_sale_ratio}")
    private BigDecimal onSaleRatio;

    @Value("${ad.auction.share_by_auctioneer_ratio}")
    private BigDecimal shareByAuctioneerRatio;

    @Value("${ad.auction.burn_ratio}")
    private BigDecimal burnRatio;

    @Value("${ad.auction.minimum_bid_ratio}")
    private BigDecimal minimumBidRatio;

    @Value("${ad.auction.maximum_bid_days}")
    private Integer maximumBidDays;

    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdAuctionService adAuctionService;

    @Autowired
    private AdAuctionRecordService adAuctionRecordService;

    @Override
    public GlobalResponse getAdAuctionList() {
        LocalDate nowDate = LocalDate.now();
        Long now = System.currentTimeMillis();
        LambdaQueryWrapper<Ad> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Ad::getStatus, AdStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper.orderByAsc(Ad::getAdIndex);
        List<Ad> list = adService.list(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no ad found");
        }
        List<Long> adIds = list.stream().map(Ad::getId).collect(Collectors.toList());
        List<AdAuctionVO> data = new ArrayList<>();

        //combine and unique index
        LambdaQueryWrapper<AdAuction> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(AdAuction::getAdId, adIds);
        lambdaQueryWrapper1.ge(AdAuction::getAuctionDate, nowDate);
        List<AdAuction> list1 = adAuctionService.list(lambdaQueryWrapper1);

        if (CollectionUtils.isEmpty(list1)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no auction found");
        }

        for (AdAuction adAuction : list1){

            Long adId = adAuction.getAdId();
            Ad ad = adService.getById(adId);

            Long adAuctionId = adAuction.getId();

            AdAuctionVO adAuctionVO = new AdAuctionVO();
            adAuctionVO.setAdAuctionId(adAuction.getId());
            adAuctionVO.setAuctionStatus(adAuction.getStatus());
            adAuctionVO.setAuctionStatusName(AdAuctionStatusEnum.getName(adAuction.getStatus()));
            adAuctionVO.setAuctionStartTime(ad.getNextAuctionTime());
            adAuctionVO.setAuctionEndTime(adAuction.getAuctionEndTime());
            ZoneId zoneId = ZoneId.systemDefault();
            adAuctionVO.setAuctionEndCountDown(adAuctionVO.getAuctionEndTime().atZone(zoneId).toInstant().toEpochMilli());
            adAuctionVO.setAuctionStartCountDown(adAuctionVO.getAuctionStartTime().atZone(zoneId).toInstant().toEpochMilli());
            adAuctionVO.setAdIndex(ad.getAdIndex());
            adAuctionVO.setLink(ad.getLink());
            adAuctionVO.setBidPrice(adAuction.getBidPrice());
            adAuctionVO.setCurrentHolderId(ad.getUserId());
            if (ad.getUserId() != null && ad.getUserId() != 0L){
                User user = userService.getById(ad.getUserId());
                adAuctionVO.setCurrentHolderName(Objects.isNull(user)? "" : user.getNickName());
                adAuctionVO.setCurrentHolderAvatarUrl(Objects.isNull(user)? "" : user.getAvatarUrl());
                adAuctionVO.setCurrentHolderMemo(Objects.isNull(user)? "" : user.getCharacterSign());
            }

            if (adAuction.getStatus() == AdAuctionStatusEnum.ONGOING.getCode()){
                LambdaQueryWrapper<AdAuctionRecord> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.select(AdAuctionRecord::getUserId, AdAuctionRecord::getMemo);
                lambdaQueryWrapper2.eq(AdAuctionRecord::getAdAuctionId, adAuctionId);
                lambdaQueryWrapper2.orderByDesc(AdAuctionRecord::getAuctionUnitPrice);
                lambdaQueryWrapper2.eq(AdAuctionRecord::getStatus, AdAuctionRecordStatusEnum.ONGOING.getCode());
                lambdaQueryWrapper2.last("limit 1");
                List<AdAuctionRecord> list2 = adAuctionRecordService.list(lambdaQueryWrapper2);
                if (list2.size() == 1){
                    AdAuctionRecord currentRecord = list2.get(0);
                    adAuctionVO.setCurrentBidderMemo(currentRecord.getMemo());
                    adAuctionVO.setCurrentBidderId(currentRecord.getUserId());
                    User currentBidder = userService.getById(currentRecord.getUserId());
                    adAuctionVO.setCurrentBidderName(Objects.isNull(currentBidder)? "" : currentBidder.getNickName());
                    adAuctionVO.setCurrentBidderAvatarUrl(Objects.isNull(currentBidder)? "" : currentBidder.getAvatarUrl());
                }
            }

            adAuctionVO.setResourceUrl(ad.getResourceUrl());
            adAuctionVO.setBuyItNowPrice(ad.getBuyItNowPrice());
            adAuctionVO.setStartPrice(ad.getStartPrice());
            adAuctionVO.setAdId(ad.getId());
            data.add(adAuctionVO);
        }
        return GlobalResponse.success(data);
    }

    @Override
    public GlobalResponse getAdAuctionInfo() {
        AdAuctionInfoVO vo = new AdAuctionInfoVO();
        vo.setShareByAuctioneerRatio(shareByAuctioneerRatio);
        vo.setBurnRatio(burnRatio);
        vo.setFeedbackRatio(feedbackRatio);
        vo.setMinimumBidRatio(minimumBidRatio);
        vo.setOnSaleRatio(onSaleRatio);
        vo.setMaximumBidDays(maximumBidDays);
        return GlobalResponse.success(vo);
    }
}
