package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.DailyStakingRecordMapper;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.request.StakeDailyStakingRequest;
import com.example.sharablead.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
@Slf4j
@Service
public class DailyStakingRecordServiceImpl extends ServiceImpl<DailyStakingRecordMapper, DailyStakingRecord> implements DailyStakingRecordService {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OpusService opusService;

    @Autowired
    private DailyStakingPoolService dailyStakingPoolService;

    @Autowired
    private BalanceEntryService balanceEntryService;

    @Autowired
    private DailyStakingRecordMapper dailyStakingRecordMapper;

    @Value("${daily-staking.single-opus-staking-numbers}")
    private Integer maxNum;

    @Override
    //TODO add lock
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse stakeDailyStaking(StakeDailyStakingRequest stakeDailyStakingRequest) {
        Long userId = stakeDailyStakingRequest.getUserId();
        Long opusId = stakeDailyStakingRequest.getOpusId();
        BigDecimal stakingAmount = stakeDailyStakingRequest.getStakingAmount();

        User user = userService.getById(userId);
        if (Objects.isNull(user) || UserStatusEnum.NORMAL.getCode() != user.getStatus()){
            log.error("stakeDailyStaking: invalid user, userId = {}", userId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user");
        }

        Opus opus = opusService.getById(opusId);
        if (Objects.isNull(opus) || OpusStatusEnum.NORMAL.getCode() != opus.getStatus()){
            log.error("stakeDailyStaking: invalid opus, opusId = {}", opusId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid opus");
        }

        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, userId);
        Account account = accountService.getOne(lambdaQueryWrapper);

        if (Objects.isNull(account) || account.getBalance().compareTo(stakingAmount) < 0){
            log.error("stakeDailyStaking: invalid account or balance insufficent, userId = {}, stakingAmount = {}", userId, stakingAmount);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid account or balance insufficent");
        }

        LambdaQueryWrapper<DailyStakingPool> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(DailyStakingPool::getOpusId, opusId);
        lambdaQueryWrapper1.eq(DailyStakingPool::getStakingDate, LocalDate.now());
        DailyStakingPool pool = dailyStakingPoolService.getOne(lambdaQueryWrapper1);

        DailyStakingRecord record = new DailyStakingRecord();
        record.setStakingAmount(stakingAmount);
        record.setUserId(userId);
        record.setStatus(DailyStakingRecordStatusEnum.UNSETTLE.getCode());
        record.setGmtCreated(LocalDateTime.now());
        record.setGmtModified(LocalDateTime.now());

        if (Objects.isNull(pool)){
            pool = new DailyStakingPool();
            pool.setId(IDUtil.nextId());
            pool.setStakingDate(LocalDate.now());
            pool.setStatus(DailyStakingPoolStatusEnum.STAKABLE.getCode());
            pool.setStakingAmount(stakingAmount);
            pool.setGmtCreated(LocalDateTime.now());
            pool.setGmtModified(LocalDateTime.now());
            pool.setOpusId(opusId);
            dailyStakingPoolService.save(pool);
        }else {
            LambdaQueryWrapper<DailyStakingRecord> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(DailyStakingRecord::getPoolId, pool.getId());
            if (dailyStakingRecordMapper.selectCount(lambdaQueryWrapper2) >= maxNum){
                log.error("stakeDailyStaking: already reached maxNum, poolId = {}, maxNum = {}", pool.getId(), maxNum);
                pool.setGmtModified(LocalDateTime.now());
                pool.setStatus(DailyStakingPoolStatusEnum.UNSTAKABLE.getCode());
                dailyStakingPoolService.updateById(pool);
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "already reached maxNum");
            }
            lambdaQueryWrapper2.eq(DailyStakingRecord::getUserId, userId);
            if (dailyStakingRecordMapper.selectCount(lambdaQueryWrapper2) >= 1){
                log.error("stakeDailyStaking: already staked, poolId = {}, userId = {}", pool.getId(), userId);
                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "already staked");
            }
            pool.setGmtModified(LocalDateTime.now());
            pool.setStakingAmount(pool.getStakingAmount().add(stakingAmount));
            dailyStakingPoolService.updateById(pool);
        }
        record.setPoolId(pool.getId());
        dailyStakingRecordMapper.insert(record);

        ChangeBalanceEntryRequest changeBalanceEntryRequest = new ChangeBalanceEntryRequest();
        changeBalanceEntryRequest.setAmount(stakingAmount);
        changeBalanceEntryRequest.setEntryEvent(AccountEntryEventEnum.DAILY_STAKING.getCode());
        changeBalanceEntryRequest.setEntryType(AccountEntryTypeEnum.OUT.getCode());
        changeBalanceEntryRequest.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
        changeBalanceEntryRequest.setUserId(userId);
        if (balanceEntryService.changeBalanceEntry(changeBalanceEntryRequest)){
            return GlobalResponse.success(true);
        }
        return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
    }
}
