package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Account;
import com.example.sharablead.entity.Reward;
import com.example.sharablead.entity.User;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.RewardMapper;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.request.PageRewardRecordRequest;
import com.example.sharablead.request.RewardMeRequest;
import com.example.sharablead.response.PageRewardRecordResponse;
import com.example.sharablead.response.PageRewardRecordVO;
import com.example.sharablead.service.AccountService;
import com.example.sharablead.service.BalanceEntryService;
import com.example.sharablead.service.RewardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.UserService;
import com.example.sharablead.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-09
 */
@Slf4j
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward> implements RewardService {

    @Autowired
    private UserService userService;

    @Autowired
    private BalanceEntryService balanceEntryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RewardMapper rewardMapper;

    @Override
    //TODO add lock
    @Transactional(rollbackFor = Exception.class)
    public GlobalResponse rewardMe(RewardMeRequest rewardMeRequest) {

        Long toUserId = rewardMeRequest.getToUserId();
        Long fromUserId = rewardMeRequest.getFromUserId();
        BigDecimal amount = rewardMeRequest.getAmount();
        String memo = rewardMeRequest.getMemo();
        Assert.notNull(toUserId, "toUserId can not be null");
        Assert.notNull(fromUserId, "fromUserId can not be null");
        Assert.notNull(amount, "amount can not be null");

        User toUser = userService.getById(toUserId);
        User fromUser = userService.getById(fromUserId);

        if (Objects.isNull(toUser) || Objects.isNull(fromUser) || toUser.getStatus() != UserStatusEnum.NORMAL.getCode() || fromUser.getStatus() != UserStatusEnum.NORMAL.getCode()){
            log.error("rewardMe: invalid user or status, toUserId = {}, fromUserId = {}", toUserId, fromUserId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user or status");
        }
        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, toUserId);
        Account toAccount = accountService.getOne(lambdaQueryWrapper);

        LambdaQueryWrapper<Account> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Account::getUserId, fromUserId);
        Account fromAccount = accountService.getOne(lambdaQueryWrapper1);

        if (Objects.isNull(toAccount) || Objects.isNull(fromAccount)){
            log.error("rewardMe: invalid account, toUserId = {}, fromUserId = {}", toUserId, fromUserId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid account");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0){
            log.error("rewardMe: balance is insufficient, balance = {}, amount = {}", fromAccount.getBalance(), amount);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "balance is insufficient");
        }

        ChangeBalanceEntryRequest toRequest = new ChangeBalanceEntryRequest();
        ChangeBalanceEntryRequest fromRequest = new ChangeBalanceEntryRequest();

        toRequest.setUserId(toUserId);
        toRequest.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
        toRequest.setAmount(amount);
        toRequest.setEntryType(AccountEntryTypeEnum.IN.getCode());
        toRequest.setEntryEvent(AccountEntryEventEnum.REWARD.getCode());

        fromRequest.setUserId(fromUserId);
        fromRequest.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
        fromRequest.setAmount(amount);
        fromRequest.setEntryType(AccountEntryTypeEnum.OUT.getCode());
        fromRequest.setEntryEvent(AccountEntryEventEnum.REWARD.getCode());

        if (balanceEntryService.changeBalanceEntry(fromRequest) && balanceEntryService.changeBalanceEntry(toRequest)){
            Reward reward = new Reward();
            reward.setAmount(amount);
            reward.setId(IDUtil.nextId());
            reward.setSynchronizeFlag(SynchronizedFlagEnum.OUT_OF_SYNCHRONIZED.getCode());
            if (StringUtils.isNotBlank(memo)){
                reward.setMemo(memo);
            }
            reward.setGmtCreated(LocalDateTime.now());
            reward.setRewardDate(LocalDate.now());
            reward.setGmtModified(LocalDateTime.now());
            reward.setFromUserId(fromUserId);
            reward.setToUserId(toUserId);
            rewardMapper.insert(reward);
            return GlobalResponse.success(true);
        }
        //rollback manually
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return GlobalResponse.error(GlobalResponseEnum.UNKNOWN_ERROR.getCode(), GlobalResponseEnum.UNKNOWN_ERROR.getMessage());
    }

    @Override
    public GlobalResponse pageRewardRecord(PageRewardRecordRequest pageRewardRecordRequest) {
        PageRewardRecordResponse response = new PageRewardRecordResponse();
        Long toUserId = pageRewardRecordRequest.getToUserId();
        Long fromUserId = pageRewardRecordRequest.getFromUserId();
        long pageNo = pageRewardRecordRequest.getPageNo() == null? 1L : pageRewardRecordRequest.getPageNo();
        long pageSize = pageRewardRecordRequest.getPageSize() == null? 5L : pageRewardRecordRequest.getPageSize();

        if (Objects.isNull(toUserId) || toUserId == 0L){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid toUserId");
        }

        if (toUserId.equals(fromUserId)){
            response.setRewardable(false);
        }

        LambdaQueryWrapper<Account> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Account::getUserId, fromUserId);
        Account account = accountService.getOne(lambdaQueryWrapper1);

        if (Objects.isNull(account)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid account");
        }

        response.setOffChainToken(account.getBalance());
        //计算累计收到打赏数 性能不好
        LambdaQueryWrapper<Reward> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Reward::getToUserId, toUserId);
        List<Reward> list = rewardMapper.selectList(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list)){
            return GlobalResponse.success(response);
        }
        response.setTotalRewardAmount(list.stream().map(Reward::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add));
        Page<Reward> page = new Page<>(pageNo, pageSize);
        lambdaQueryWrapper.orderByDesc(Reward::getGmtCreated);
        Page<Reward> result = rewardMapper.selectPage(page, lambdaQueryWrapper);

        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalPages(result.getPages());
        response.setTotalSize(result.getTotal());

        List<Long> fromUserIds = result.getRecords().stream().map(Reward::getFromUserId).collect(Collectors.toList());
        Map<Long, User> map = userService.getUserMap(fromUserIds);
        List<PageRewardRecordVO> data = new ArrayList<>();
        result.getRecords().forEach(record ->{
            PageRewardRecordVO vo = new PageRewardRecordVO();
            vo.setAmount(record.getAmount());
            vo.setGmtCreated(record.getGmtCreated());
            vo.setFromUserId(record.getFromUserId());
            vo.setToUserId(record.getToUserId());
            vo.setFromUserName(map.containsKey(vo.getFromUserId())? map.get(vo.getFromUserId()).getNickName() : "");
            data.add(vo);
        });
        response.setData(data);
        return GlobalResponse.success(response);
    }
}
