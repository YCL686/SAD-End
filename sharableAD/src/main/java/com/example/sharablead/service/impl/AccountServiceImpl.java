package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Account;
import com.example.sharablead.entity.AccountEntry;
import com.example.sharablead.entity.User;
import com.example.sharablead.entity.WithdrawRequestRecord;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.AccountEntryMapper;
import com.example.sharablead.mapper.AccountMapper;
import com.example.sharablead.mapper.UserMapper;
import com.example.sharablead.request.ChangeBalanceEntryRequest;
import com.example.sharablead.request.DepositRequest;
import com.example.sharablead.request.WithdrawRequest;
import com.example.sharablead.service.AccountService;
import com.example.sharablead.service.BalanceEntryService;
import com.example.sharablead.service.Web3jService;
import com.example.sharablead.service.WithdrawRequestRecordService;
import com.example.sharablead.util.IDUtil;
import com.example.sharablead.util.SignCheckUtil;
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
 * @since 2022-10-13
 */
@Slf4j
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountEntryMapper accountEntryMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private Web3jService web3jService;

    @Autowired
    private BalanceEntryService balanceEntryService;

    @Autowired
    private WithdrawRequestRecordService withdrawRequestRecordService;

    @Value("${withdraw.request.delay-days}")
    private Integer delayDays;

    @Override
    public GlobalResponse getAccount(Long userId) {
        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, userId);
        Account account = accountMapper.selectOne(lambdaQueryWrapper);

        if (Objects.isNull(account)){
            log.error("account does not exist by userId = {}", userId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "account does not exist");
        }
        return GlobalResponse.success(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    //TODO need lock
    public GlobalResponse deposit(DepositRequest depositRequest) {
        String address = depositRequest.getAddress();
        String message = depositRequest.getMessage();
        String signature = depositRequest.getSignature();
        String txHash = depositRequest.getHash();
        BigDecimal amount = depositRequest.getAmount();
        Long userId = depositRequest.getUserId();

        //TODO amount min/max limit

        //signature check
        if (!SignCheckUtil.validate(signature,address,message)){
            log.error("deposit: signature is invalid");
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "signature is invalid");
        }

        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)){
            log.error("deposit: user does not exist");
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "user does not exist");
        }
        //TODO status check maybe some users could not deposit
        //if (!("xx").equals(user.getStatus))

        LambdaQueryWrapper<Account> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Account::getUserId, userId);
        lambdaQueryWrapper1.select(Account::getId, Account::getBalance);
        Account account = accountMapper.selectOne(lambdaQueryWrapper1);

        if (Objects.isNull(account)){
            log.error("deposit: account does not exist");
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "account does not exist");
        }

        Long accountId = account.getId();

        LambdaQueryWrapper<AccountEntry> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(AccountEntry::getTxHash, txHash);
        AccountEntry entry = accountEntryMapper.selectOne(lambdaQueryWrapper2);

        if (Objects.nonNull(entry)){
            log.error("deposit: txHash already exist, txHash = {}", txHash);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "txHash already exist");
        }

        account.setBalance(account.getBalance().add(amount));
        account.setGmtModified(LocalDateTime.now());
        accountMapper.updateById(account);

        entry = new AccountEntry();
        entry.setAccountId(accountId);
        entry.setId(IDUtil.nextId());
        entry.setEntryAmount(amount);
        entry.setStatus(AccountEntryStatusEnum.SUCCESS.getCode());
        entry.setEntryBalance(account.getBalance());
        entry.setTxHash(txHash);
        entry.setEntryType(AccountEntryTypeEnum.IN.getCode());
        entry.setEntryEvent(AccountEntryEventEnum.DEPOSIT.getCode());
        entry.setGmtCreated(LocalDateTime.now());
        entry.setGmtModified(LocalDateTime.now());
        accountEntryMapper.insert(entry);
        return GlobalResponse.success();
    }

    @Override
    public GlobalResponse withdraw(WithdrawRequest withdrawRequest) {
        String address = withdrawRequest.getAddress();
        String message = withdrawRequest.getMessage();
        String signature = withdrawRequest.getSignature();
        BigDecimal amount = withdrawRequest.getAmount();
        Long userId = withdrawRequest.getUserId();

        //TODO amount min/max limit
        //signature check
        if (!SignCheckUtil.validate(signature,address,message)){
            log.error("withdraw: signature is invalid");
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "signature is invalid");
        }

        User user = userMapper.selectById(userId);

        if (Objects.isNull(user)){
            log.error("withdraw: user does not exist");
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "user does not exist");
        }
        //TODO status check maybe some users could not deposit
        //if (!("xx").equals(user.getStatus))

        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, userId);
        Account account = accountMapper.selectOne(lambdaQueryWrapper);

        if (Objects.isNull(account)){
            log.error("withdraw: account does not exist");
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "account does not exist");
        }

        Long accountId = account.getId();
        BigDecimal balance = account.getBalance();

        if (Objects.isNull(balance) || balance.compareTo(amount) < 0){
            log.error("withdraw: account balance inSufficient, userId = {}, accountId = {}", userId, accountId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "account balance inSufficient");
        }

        LambdaQueryWrapper<WithdrawRequestRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(WithdrawRequestRecord::getAccountId, accountId);
        lambdaQueryWrapper1.eq(WithdrawRequestRecord::getStatus, WithdrawRequestStatusEnum.PENDING.getCode());
        if (withdrawRequestRecordService.count(lambdaQueryWrapper1) > 0){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "only single withdraw could be process at a time");
        }

        ChangeBalanceEntryRequest changeBalanceEntryRequest = new ChangeBalanceEntryRequest();
        Long entryId = IDUtil.nextId();
        changeBalanceEntryRequest.setEntryId(entryId);
        changeBalanceEntryRequest.setUserId(userId);
        changeBalanceEntryRequest.setAmount(amount);
        changeBalanceEntryRequest.setEntryType(AccountEntryTypeEnum.OUT.getCode());
        changeBalanceEntryRequest.setEntryEvent(AccountEntryEventEnum.WITHDRAW.getCode());
        changeBalanceEntryRequest.setStatus(AccountEntryStatusEnum.PENDING.getCode());

        if (balanceEntryService.changeBalanceEntry(changeBalanceEntryRequest)){
            WithdrawRequestRecord record = new WithdrawRequestRecord();
            record.setAccountId(accountId);
            record.setId(IDUtil.nextId());
            record.setWithdrawDate(LocalDate.now().plusDays(delayDays));
            record.setGmtCreated(LocalDateTime.now());
            record.setGmtModified(LocalDateTime.now());
            record.setEntryId(entryId);
            record.setWithdrawAddress(user.getAddress());
            record.setWithdrawAmount(amount);
            record.setStatus(WithdrawRequestStatusEnum.PENDING.getCode());
            withdrawRequestRecordService.save(record);
            return GlobalResponse.success(true);
        }

        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "Please Try Later");
    }
}
