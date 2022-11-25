package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.entity.Account;
import com.example.sharablead.entity.AccountEntry;
import com.example.sharablead.entity.User;
import com.example.sharablead.enums.*;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.service.AccountEntryService;
import com.example.sharablead.service.AccountService;
import com.example.sharablead.service.BalanceEntryService;
import com.example.sharablead.service.UserService;
import com.example.sharablead.util.IDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
@Slf4j
@Service
public class BalanceEntryServiceImpl implements BalanceEntryService {

    /**
     * balanceChange method is common here, do not write new method to operate balance
     * balance change cases：deposit、withdraw、dailyTask、dailyStaking、rewards、etc...
     * @param request
     * @return
     */
    private static final String NULL = "-";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountEntryService accountEntryService;

    @Autowired
    private UserService userService;

    @Override
    public boolean changeBalanceEntry(ChangeBalanceEntryRequest request) {

        Long entryId = request.getEntryId();
        BigDecimal amount = request.getAmount();
        Integer entryType = request.getEntryType();
        Integer entryEvent = request.getEntryEvent();
        String txHash = request.getTxHash();
        Long userId = request.getUserId();
        Integer status = request.getStatus();

        Assert.notNull(amount, "amount can not be null");
        Assert.notNull(entryType, "entryType can not be null");
        Assert.notNull(entryEvent, "entryEvent can not be null");
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(status, "status can not be null");

        User user = userService.getById(userId);
        if (Objects.isNull(user) || UserStatusEnum.BAN.getCode() == user.getStatus()){
            log.error("changeBalance: user does not exist or user status is banned, userId = {}", userId);
            return false;
        }

        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, userId);

        Account account = accountService.getOne(lambdaQueryWrapper);
        if (Objects.isNull(account)){
            log.error("changeBalance: account does not exist, userId = {}", userId);
            return false;
        }

        if (entryType != AccountEntryTypeEnum.IN.getCode() && entryType != AccountEntryTypeEnum.OUT.getCode()){
            log.error("changeBalance: invalid entryType, entryType = {}", entryType);
            return false;
        }

        if (NULL.equals(AccountEntryEventEnum.getName(entryEvent))){
            log.error("changeBalance: invalid entryEvent, entryEvent = {}", entryEvent);
            return false;
        }

        //TODO what about failed record, in fact it should not appear, but what if it happens, how to process
        if (status != AccountEntryStatusEnum.SUCCESS.getCode() && status != AccountEntryStatusEnum.PENDING.getCode()){
            log.error("changeBalance: invalid status, status = {}", status);
            return false;
        }

        if (entryType == AccountEntryTypeEnum.IN.getCode()){
            account.setBalance(account.getBalance().add(amount));
        }

        if (entryType == AccountEntryTypeEnum.OUT.getCode()){
            account.setBalance(account.getBalance().subtract(amount));
        }

        if (StringUtils.isNotBlank(txHash)){
            LambdaQueryWrapper<AccountEntry> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(AccountEntry::getTxHash, txHash);
            if(accountEntryService.count(lambdaQueryWrapper1) > 0){
                log.error("txHash already exist, txHash = {}", txHash);
                return false;
            }
            //TODO 可能需要根据txHash去链上查询数据是否正确 对账
        }

        account.setGmtModified(LocalDateTime.now());
        accountService.updateById(account);

        AccountEntry entry = new AccountEntry();
        if (entryId != null && entryId != 0L){
            entry.setId(entryId);
        }else {
            entry.setId(IDUtil.nextId());
        }
        entry.setGmtCreated(LocalDateTime.now());
        entry.setGmtModified(LocalDateTime.now());
        entry.setEntryEvent(entryEvent);
        if (StringUtils.isNotBlank(txHash)){
            entry.setTxHash(txHash);
        }
        entry.setEntryBalance(account.getBalance());
        entry.setEntryType(entryType);
        entry.setStatus(status);
        entry.setAccountId(account.getId());
        entry.setEntryAmount(amount);
        accountEntryService.save(entry);
        return true;
    }
}
