package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.*;
import com.example.sharablead.service.*;
import com.example.sharablead.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author YCL686
 * @Date 2022/10/23
 */
@Slf4j
@Service
public class SpecificCronJobServiceImpl implements SpecificCronJobService {

    @Autowired
    private UserService userService;

    @Autowired
    private OpusService opusService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private FocusService focusService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private AccountService accountService;

    @Value("${calculate.score-weight.active.opus}")
    private BigDecimal activeOpusWeight;

    @Value("${calculate.score-weight.active.like}")
    private BigDecimal activeLikeWeight;

    @Value("${calculate.score-weight.active.comment}")
    private BigDecimal activeCommentWeight;

    @Value("${calculate.score-weight.active.focus}")
    private BigDecimal activeFocusWeight;

    @Value("${calculate.score-weight.active.collect}")
    private BigDecimal activeCollectWeight;

    @Value("${calculate.score-weight.active.offChainToken}")
    private BigDecimal activeOffChainTokenWeight;

    @Value("${calculate.score-weight.hot.liked}")
    private BigDecimal hotLikedWeight;

    @Value("${calculate.score-weight.hot.commented}")
    private BigDecimal hotCommentedWeight;

    @Value("${calculate.score-weight.hot.watched}")
    private BigDecimal hotWatchedWeight;

    @Value("${calculate.score-weight.hot.collected}")
    private BigDecimal hotCollectedWeight;

    @Value("${calculate.score-weight.hot.G}")
    private BigDecimal G;

    @Autowired
    private AdService adService;

    @Autowired
    private AdAuctionService adAuctionService;

    @Autowired
    private AdAuctionRecordService adAuctionRecordService;

    @Autowired
    private SynchronizeService synchronizeService;

    @Autowired
    private SynchronizeRecordService synchronizeRecordService;

    @Autowired
    private SynchronizeConfigService synchronizeConfigService;

    @Autowired
    private DailyTaskConfigService dailyTaskConfigService;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private Web3jService web3jService;

    @Value("${web3.wallet.on-sale.address}")
    private String onSaleWalletAddress;

    @Value("${web3.wallet.burn.address}")
    private String burnAddress;

    @Value("${web3.wallet.daily-task.address}")
    private String feedBackAddress;

    @Value("${web3.wallet.deposit-withdraw.address}")
    private String depositWithdrawAddress;

    @Value("${daily-task.continue_days}")
    private Integer continueDays;

    @Autowired
    private WithdrawRequestRecordService withdrawRequestRecordService;

    @Autowired
    private AccountEntryService accountEntryService;


    @Override
    public void calculateActiveScore() {

        long pageNo = 1;
        long pageSize = 5;

        Page<User> page = new Page<>(pageNo, pageSize);
        List<User> list = userService.page(page, null).getRecords();
        LocalDateTime now = LocalDateTime.now();

        while (!CollectionUtils.isEmpty(list)) {
            for (User user : list) {
                Long userId = user.getId();

                LambdaQueryWrapper<Opus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Opus::getUserId, userId);
                lambdaQueryWrapper.eq(Opus::getStatus, OpusStatusEnum.NORMAL.getCode());
                lambdaQueryWrapper.ge(Opus::getGmtCreated, now.minusDays(7));
                BigDecimal opusCount = BigDecimal.valueOf(opusService.count(lambdaQueryWrapper));

                LambdaQueryWrapper<Like> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(Like::getUserId, userId);
                lambdaQueryWrapper1.ge(Like::getGmtCreated, now.minusDays(7));
                BigDecimal likeCount = BigDecimal.valueOf(likeService.count(lambdaQueryWrapper1));

                LambdaQueryWrapper<Comment> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(Comment::getFromUserId, userId);
                lambdaQueryWrapper2.ge(Comment::getGmtCreated, now.minusDays(7));
                BigDecimal commentCount = BigDecimal.valueOf(commentService.count(lambdaQueryWrapper2));

                LambdaQueryWrapper<Focus> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper3.eq(Focus::getUserId, userId);
                lambdaQueryWrapper3.eq(Focus::getStatus, FocusStatusEnum.NORMAL.getCode());
                lambdaQueryWrapper3.ge(Focus::getGmtCreated, now.minusDays(7));
                BigDecimal focusCount = BigDecimal.valueOf(focusService.count(lambdaQueryWrapper3));

                LambdaQueryWrapper<Collect> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper4.eq(Collect::getUserId, userId);
                lambdaQueryWrapper4.ge(Collect::getGmtCreated, now.minusDays(7));
                BigDecimal collectCount = BigDecimal.valueOf(collectService.count(lambdaQueryWrapper4));

                LambdaQueryWrapper<Account> lambdaQueryWrapper5 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper5.eq(Account::getUserId, userId);
                lambdaQueryWrapper5.select(Account::getBalance);
                Account account = accountService.getOne(lambdaQueryWrapper5);
                BigDecimal offChainTokenCount = Objects.isNull(account) ? BigDecimal.ZERO : account.getBalance();

                BigDecimal sumCount = opusCount.add(likeCount).add(commentCount).add(focusCount).add(collectCount).add(offChainTokenCount);
                BigDecimal activeScore = BigDecimal.ZERO;
                if (!BigDecimal.ZERO.equals(sumCount)) {
                    activeScore = (opusCount.multiply(activeOpusWeight)
                            .add(likeCount.multiply(activeLikeWeight))
                            .add(commentCount.multiply(activeCommentWeight))
                            .add(focusCount.multiply(activeFocusWeight))
                            .add(collectCount.multiply(activeCollectWeight))
                            .add(offChainTokenCount.multiply(activeOffChainTokenWeight)))
                            .divide(sumCount, 2, RoundingMode.HALF_UP);
                }


                user.setActiveScore(activeScore);
                user.setGmtModified(now);
                userService.updateById(user);

            }
            pageNo++;
            page = new Page<>(pageNo, pageSize);
            list = userService.page(page, null).getRecords();
        }

    }

    @Override
    public void calculateHotScore() {

        long pageNo = 1;
        long pageSize = 5;

        Page<Opus> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Opus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Opus::getStatus, OpusStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper.isNotNull(Opus::getPublishTime);
        List<Opus> list = opusService.page(page, lambdaQueryWrapper).getRecords();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayZero = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        //hot-score = (W + I)/(T+1)^G W为被动指标 即用户的行为 I为主动指标 即创作者的行为 T为时间差 G为重力系数
        while (!CollectionUtils.isEmpty(list)) {
            for (Opus opus : list) {
                Long opusId = opus.getId();
                Long userId = opus.getUserId();

                BigDecimal I = BigDecimal.ZERO;
                LambdaQueryWrapper<User> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(User::getId, userId);
                lambdaQueryWrapper1.select(User::getActiveScore);
                User user = userService.getOne(lambdaQueryWrapper1);
                if (Objects.nonNull(user)) {
                    I = user.getActiveScore();
                }

                Duration duration = Duration.between(opus.getPublishTime(), now);
                BigDecimal T = BigDecimal.valueOf(duration.toHours());

                LambdaQueryWrapper<Like> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(Like::getLikedId, opusId);
                lambdaQueryWrapper2.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
                lambdaQueryWrapper2.eq(Like::getLikedType, LikedTypeEnum.OPUS.getCode());
                lambdaQueryWrapper2.ge(Like::getGmtCreated, todayZero);
                BigDecimal hotLikedCount = BigDecimal.valueOf(likeService.count(lambdaQueryWrapper2));

                LambdaQueryWrapper<Comment> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper3.eq(Comment::getOpusId, opusId);
                lambdaQueryWrapper3.eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode());
                lambdaQueryWrapper3.ge(Comment::getGmtCreated, todayZero);
                BigDecimal hotCommentedCount = BigDecimal.valueOf(commentService.count(lambdaQueryWrapper3));

                LambdaQueryWrapper<Watch> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper4.eq(Watch::getWatchedId, opusId);
                lambdaQueryWrapper4.eq(Watch::getWatchedType, WatchedTypeEnum.OPUS.getCode());
                lambdaQueryWrapper4.ge(Watch::getGmtCreated, todayZero);
                BigDecimal hotWatchedCount = BigDecimal.valueOf(watchService.count(lambdaQueryWrapper4));

                LambdaQueryWrapper<Collect> lambdaQueryWrapper5 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper5.eq(Collect::getOpusId, opusId);
                lambdaQueryWrapper5.eq(Collect::getStatus, CollectStatusEnum.NORMAL.getCode());
                lambdaQueryWrapper5.ge(Collect::getGmtCreated, todayZero);
                BigDecimal hotCollectedCount = BigDecimal.valueOf(collectService.count(lambdaQueryWrapper5));

                BigDecimal sumCount = hotLikedCount.add(hotCommentedCount).add(hotWatchedCount).add(hotCollectedCount);
                BigDecimal W = BigDecimal.ZERO;
                if (!sumCount.equals(BigDecimal.ZERO)) {
                    W = hotCollectedWeight.multiply(hotLikedCount)
                            .add(hotCommentedWeight).multiply(hotCommentedCount)
                            .add(hotWatchedWeight).multiply(hotWatchedCount)
                            .add(hotCollectedWeight).multiply(hotCollectedCount)
                            .divide(sumCount, 2, RoundingMode.HALF_UP);
                }

                BigDecimal divided = BigDecimal.valueOf(Math.pow(T.add(BigDecimal.ONE).doubleValue(), G.doubleValue()));

                BigDecimal hotScore = W.add(I).divide(divided, 2, RoundingMode.HALF_UP);
                opus.setHotScore(hotScore);
                opus.setGmtModified(LocalDateTime.now());
                opusService.updateById(opus);


            }

            pageNo++;
            page = new Page<>(pageNo, pageSize);
            list = opusService.page(page, lambdaQueryWrapper).getRecords();
        }

    }

    @Override
    public void calculateDailyTaskReward() {
        GlobalResponse dailyTaskResponse = web3jService.getBalanceOf(feedBackAddress);
        if (dailyTaskResponse.getCode() == GlobalResponseEnum.SUCCESS.getCode()) {
            BigDecimal balance = (BigDecimal) dailyTaskResponse.getData();
            long userCount = userService.count();
            BigDecimal singleTotalAmount = balance.divide(BigDecimal.valueOf(continueDays), 2, RoundingMode.HALF_DOWN).divide(BigDecimal.valueOf(userCount), 2, RoundingMode.HALF_DOWN);
            LambdaQueryWrapper<DailyTaskConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DailyTaskConfig::getTaskStatus, DailyTaskConfigStatusEnum.ONLINE.getCode());
            List<DailyTaskConfig> list = dailyTaskConfigService.list(lambdaQueryWrapper);
            for (DailyTaskConfig config : list) {
                config.setTaskReward(singleTotalAmount.multiply(config.getTaskRatio()).setScale(2, RoundingMode.HALF_DOWN));
                config.setGmtModified(LocalDateTime.now());
                dailyTaskConfigService.updateById(config);
            }
        }
    }

    @Override
    public void synchronizeWallet() {
        LocalDate nowDate = LocalDate.now().minusDays(1);

        //Keep Idempotent
        LambdaQueryWrapper<Synchronize> synchronizeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        synchronizeLambdaQueryWrapper.eq(Synchronize::getSynchronizeDate, nowDate);
        if (synchronizeService.count(synchronizeLambdaQueryWrapper) > 0){
            log.error("syncWallet: synchronize already exist, nowDate = {}", nowDate);
            return;
        }

        List<SynchronizeConfig> configList = synchronizeConfigService.list();

        Synchronize synchronize = new Synchronize();
        synchronize.setId(IDUtil.nextId());
        synchronize.setSynchronizeDate(nowDate);
        synchronize.setGmtCreated(LocalDateTime.now());
        synchronize.setGmtModified(LocalDateTime.now());
        synchronize.setSynchronizeAmount(BigDecimal.ZERO);

        for (SynchronizeConfig config : configList) {
            BigDecimal totalRatio = config.getTotalRatio();
            BigDecimal feedBackRatio = config.getFeedBackRatio();
            BigDecimal onSaleRatio = config.getOnSaleRatio();
            BigDecimal burnRatio = config.getBurnRatio();

            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal synchronizeAmount = BigDecimal.ZERO;
            BigDecimal feedBackAmount = BigDecimal.ZERO;
            BigDecimal onSaleAmount = BigDecimal.ZERO;
            BigDecimal burnAmount = BigDecimal.ZERO;

            LambdaQueryWrapper<SynchronizeRecord> synchronizeRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
            synchronizeRecordLambdaQueryWrapper.eq(SynchronizeRecord::getSynchronizeId, synchronize.getId());
            synchronizeRecordLambdaQueryWrapper.eq(SynchronizeRecord::getSynchronizeType, config.getSynchronizeType());
            if (synchronizeRecordService.count(synchronizeRecordLambdaQueryWrapper) > 0){
                log.error("syncWallet: synchronize record already exist, nowDate = {}, synchronizeId = {}, synchronizeType = {}", nowDate, synchronize.getId(), config.getSynchronizeType());
                continue;
            }
            //reward sync
            if (SynchronizeTypeEnum.REWARD.getCode() == config.getSynchronizeType()) {
                LambdaQueryWrapper<Reward> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.select(Reward::getAmount);
                lambdaQueryWrapper.eq(Reward::getRewardDate, nowDate);
                lambdaQueryWrapper.eq(Reward::getSynchronizeFlag, SynchronizedFlagEnum.OUT_OF_SYNCHRONIZED.getCode());
                //TODO now is query all the data, it needs to pagination later
                List<Reward> list = rewardService.list(lambdaQueryWrapper);
                for (Reward reward : list) {
                    totalAmount = totalAmount.add(reward.getAmount());
                }
            }

            //ad auction sync
            if (SynchronizeTypeEnum.AD_AUCTION.getCode() == config.getSynchronizeType()) {
                LambdaQueryWrapper<AdAuctionRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(AdAuctionRecord::getAuctionDate, nowDate);
                lambdaQueryWrapper.eq(AdAuctionRecord::getStatus, AdAuctionRecordStatusEnum.SUCCESS.getCode());
                lambdaQueryWrapper.select(AdAuctionRecord::getAuctionTotalPrice);
                List<AdAuctionRecord> list = adAuctionRecordService.list(lambdaQueryWrapper);
                for (AdAuctionRecord record : list) {
                    totalAmount = totalAmount.add(record.getAuctionTotalPrice());
                }
            }

            //TODO daily-staking sync
            if (SynchronizeTypeEnum.DAILY_STAKING.getCode() == config.getSynchronizeType()) {

            }

            //TODO nft trade sync
            if (SynchronizeTypeEnum.NFT.getCode() == config.getSynchronizeType()) {

            }

            synchronizeAmount = totalAmount.multiply(totalRatio);
            feedBackAmount = synchronizeAmount.multiply(feedBackRatio);
            onSaleAmount = synchronizeAmount.multiply(onSaleRatio);
            burnAmount = synchronizeAmount.multiply(burnRatio);
            synchronize.setSynchronizeAmount(synchronize.getSynchronizeAmount().add(synchronizeAmount));

            SynchronizeRecord synchronizeRecord = new SynchronizeRecord();
            synchronizeRecord.setId(IDUtil.nextId());
            synchronizeRecord.setSynchronizeAmount(synchronizeAmount);
            synchronizeRecord.setTotalRatio(totalRatio);
            synchronizeRecord.setTotalAmount(totalAmount);
            synchronizeRecord.setSynchronizeId(synchronize.getId());
            synchronizeRecord.setSynchronizeType(config.getSynchronizeType());
            synchronizeRecord.setFeedBackAmount(feedBackAmount);
            synchronizeRecord.setBurnAmount(burnAmount);
            synchronizeRecord.setOnSaleAmount(onSaleAmount);
            synchronizeRecord.setFeedBackRatio(feedBackRatio);
            synchronizeRecord.setBurnRatio(burnRatio);
            synchronizeRecord.setOnSaleRatio(onSaleRatio);
            synchronizeRecord.setBurnMemo("-");
            synchronizeRecord.setBurnStatus(SynchronizeStatusEnum.NULL.getCode());
            synchronizeRecord.setBurnTxHash("-");

            synchronizeRecord.setOnSaleMemo("-");
            synchronizeRecord.setOnSaleStatus(SynchronizeStatusEnum.NULL.getCode());
            synchronizeRecord.setOnSaleTxHash("-");

            synchronizeRecord.setFeedBackMemo("-");
            synchronizeRecord.setFeedBackStatus(SynchronizeStatusEnum.NULL.getCode());
            synchronizeRecord.setFeedBackTxHash("-");

            if (burnAmount.compareTo(BigDecimal.ZERO) > 0){
                GlobalResponse burnResponse = web3jService.synchronize(depositWithdrawAddress, burnAddress, burnAmount);
                if (burnResponse.getCode() == GlobalResponseEnum.SUCCESS.getCode()) {
                    synchronizeRecord.setBurnStatus(SynchronizeStatusEnum.SUCCESS.getCode());
                    synchronizeRecord.setBurnMemo("-");
                    synchronizeRecord.setBurnTxHash(burnResponse.getData().toString());
                } else {
                    synchronizeRecord.setBurnStatus(SynchronizeStatusEnum.FAIL.getCode());
                    synchronizeRecord.setBurnMemo(burnResponse.getMessage());
                    synchronizeRecord.setBurnTxHash("-");
                }
            }

            if (feedBackAmount.compareTo(BigDecimal.ZERO) > 0){
                GlobalResponse feedBackResponse = new GlobalResponse();
                if (SynchronizeTypeEnum.DAILY_TASK.getCode() == config.getSynchronizeType()) { //reverse hardCode
                    feedBackResponse = web3jService.synchronize(feedBackAddress, depositWithdrawAddress, feedBackAmount);
                } else {
                    feedBackResponse = web3jService.synchronize(depositWithdrawAddress, feedBackAddress, feedBackAmount);
                }
                if (feedBackResponse.getCode() == GlobalResponseEnum.SUCCESS.getCode()) {
                    synchronizeRecord.setFeedBackStatus(SynchronizeStatusEnum.SUCCESS.getCode());
                    synchronizeRecord.setFeedBackMemo("-");
                    synchronizeRecord.setFeedBackTxHash(feedBackResponse.getData().toString());
                } else {
                    synchronizeRecord.setFeedBackStatus(SynchronizeStatusEnum.FAIL.getCode());
                    synchronizeRecord.setFeedBackMemo(feedBackResponse.getMessage());
                    synchronizeRecord.setFeedBackTxHash("-");
                }
            }

            if (onSaleAmount.compareTo(BigDecimal.ZERO) > 0){
                GlobalResponse onSaleResponse = web3jService.synchronize(depositWithdrawAddress, onSaleWalletAddress, onSaleAmount);
                if (onSaleResponse.getCode() == GlobalResponseEnum.SUCCESS.getCode()) {
                    synchronizeRecord.setOnSaleStatus(SynchronizeStatusEnum.SUCCESS.getCode());
                    synchronizeRecord.setOnSaleMemo("-");
                    synchronizeRecord.setOnSaleTxHash(onSaleResponse.getData().toString());
                } else {
                    synchronizeRecord.setOnSaleStatus(SynchronizeStatusEnum.FAIL.getCode());
                    synchronizeRecord.setOnSaleMemo(onSaleResponse.getMessage());
                    synchronizeRecord.setOnSaleTxHash("-");
                }
            }

            synchronizeRecord.setGmtCreated(LocalDateTime.now());
            synchronizeRecord.setGmtModified(LocalDateTime.now());
            synchronizeRecordService.save(synchronizeRecord);
        }
        synchronizeService.save(synchronize);
    }

    @Override
    public void processWithdrawRequest() {
        long pageNo = 1;
        long pageSize = 5;

        LocalDate now = LocalDate.now();

        Page<WithdrawRequestRecord> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<WithdrawRequestRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WithdrawRequestRecord::getStatus, WithdrawRequestStatusEnum.PENDING.getCode());
        lambdaQueryWrapper.eq(WithdrawRequestRecord::getWithdrawDate, now);
        List<WithdrawRequestRecord> list = withdrawRequestRecordService.page(page, lambdaQueryWrapper).getRecords();
        while (!CollectionUtils.isEmpty(list)) {
            for (WithdrawRequestRecord record : list) {
                String withdrawAddress = record.getWithdrawAddress();
                BigDecimal withdrawAmount = record.getWithdrawAmount();
                GlobalResponse withdrawResponse = web3jService.withdraw(withdrawAddress, withdrawAmount);
                Long entryId = record.getEntryId();

                AccountEntry entry = accountEntryService.getById(entryId);

                if (Objects.isNull(entry) || entry.getStatus() != AccountEntryStatusEnum.PENDING.getCode()) {
                    log.error("processWithdrawRequest: invalid entryId, {}", entryId);
                    continue;
                }

                if (withdrawResponse.getCode() == GlobalResponseEnum.SUCCESS.getCode()) {
                    record.setTxHash(withdrawResponse.getData().toString());
                    record.setStatus(WithdrawRequestStatusEnum.SUCCESS.getCode());
                    record.setMemo("-");
                    entry.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
                    entry.setTxHash(withdrawResponse.getData().toString());
                } else {
                    record.setStatus(WithdrawRequestStatusEnum.FAIL.getCode());
                    record.setMemo(withdrawResponse.getMessage());
                    entry.setStatus(AccountEntryStatusEnum.FAIL.getCode());
                    //TODO rnm 退钱
                }
                record.setGmtModified(LocalDateTime.now());
                withdrawRequestRecordService.updateById(record);
                accountEntryService.updateById(entry);
            }
            pageNo++;
            page = new Page<>(pageNo, pageSize);
            list = withdrawRequestRecordService.page(page, lambdaQueryWrapper).getRecords();
        }
    }

    @Override
    public void processAdAuctionBusiness() {
        //先处理昨日的拍卖
        LocalDate nowDate = LocalDate.now().minusDays(1);
        LambdaQueryWrapper<AdAuction> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AdAuction::getAuctionDate, nowDate);
        List<AdAuction> list = adAuctionService.list(lambdaQueryWrapper);
        if (!CollectionUtils.isEmpty(list)) {
            for (AdAuction adAuction : list) {
                Long adId = adAuction.getAdId();
                Long adAuctionId = adAuction.getId();
                LambdaQueryWrapper<AdAuctionRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(AdAuctionRecord::getAdAuctionId, adAuctionId);
                lambdaQueryWrapper1.orderByDesc(AdAuctionRecord::getAuctionUnitPrice);
                lambdaQueryWrapper1.last("limit 1");
                List<AdAuctionRecord> list1 = adAuctionRecordService.list(lambdaQueryWrapper1);

                Ad ad = adService.getById(adId);
                if (Objects.isNull(ad)) {
                    return;
                }

                if (list1.size() == 1) {
                    AdAuctionRecord record = list1.get(0);
                    //竞标成功 考虑一口价还是拍卖 取天数 更新t_ad表的下次拍卖时间
                    ad.setUserId(record.getUserId());
                    Integer days = record.getAuctionDays();
                    LocalDateTime nextAuctionTime = nowDate.atTime(0, 0, 0, 0).plusDays(Long.valueOf(days));
                    ad.setNextAuctionTime(nextAuctionTime);
                    ad.setEditCount(3);
                    //更新当天拍卖
                    adAuction.setDealDays(days);
                    adAuction.setTotalPrice(record.getAuctionTotalPrice().multiply(BigDecimal.valueOf(days)));
                    adAuction.setStatus(AdAuctionStatusEnum.SUCCESS.getCode());
                    adAuction.setDealPrice(record.getAuctionUnitPrice());
                    //TODO token处理
                } else { //流拍
                    LocalDateTime nextAuctionTime = nowDate.plusDays(1).atTime(0, 0, 0, 0);
                    //流拍情况下说明价格偏高 是否考虑降低起拍价
                    ad.setNextAuctionTime(nextAuctionTime);
                    ad.setUserId(0L);
                    ad.setGmtModified(LocalDateTime.now());

                    adAuction.setStatus(AdAuctionStatusEnum.FLOP.getCode());
                    adAuction.setGmtModified(LocalDateTime.now());
                }
                adService.updateById(ad);
                adAuctionService.updateById(adAuction);
                //创建新的拍卖
                AdAuction newAdAuction = new AdAuction();
                newAdAuction.setId(IDUtil.nextId());
                newAdAuction.setAuctionDate(ad.getNextAuctionTime().toLocalDate());
                newAdAuction.setAdId(adId);
                newAdAuction.setTotalPrice(BigDecimal.ZERO);
                newAdAuction.setBidPrice(BigDecimal.ZERO);
                newAdAuction.setDealDays(0);
                newAdAuction.setDealPrice(BigDecimal.ZERO);
                newAdAuction.setGmtCreated(LocalDateTime.now());
                newAdAuction.setGmtModified(LocalDateTime.now());
                //当前日期加一天再减1秒 即为拍卖结束时间
                newAdAuction.setAuctionEndTime(ad.getNextAuctionTime().plusDays(1).minusSeconds(1));
                if (nowDate.plusDays(1).isEqual(ad.getNextAuctionTime().toLocalDate())) {
                    newAdAuction.setStatus(AdAuctionStatusEnum.ONGOING.getCode());
                } else {
                    newAdAuction.setStatus(AdAuctionStatusEnum.NOT_START.getCode());
                }
                adAuctionService.save(newAdAuction);
            }
        }

        //再处理今天开始的拍卖
        LambdaQueryWrapper<AdAuction> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(AdAuction::getAuctionDate, nowDate.plusDays(1));
        lambdaQueryWrapper1.select(AdAuction::getId);
        List<AdAuction> list1 = adAuctionService.list(lambdaQueryWrapper1);

        if (!CollectionUtils.isEmpty(list1)) {
            for (AdAuction adAuction : list1) {
                adAuction.setStatus(AdAuctionStatusEnum.ONGOING.getCode());
                adAuction.setGmtModified(LocalDateTime.now());
                adAuctionService.updateById(adAuction);
            }
        }
    }
}
