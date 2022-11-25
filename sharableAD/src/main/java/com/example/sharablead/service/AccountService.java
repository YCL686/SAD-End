package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Account;
import com.example.sharablead.request.DepositRequest;
import com.example.sharablead.request.WithdrawRequest;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-13
 */
public interface AccountService extends IService<Account> {

    GlobalResponse getAccount(Long userId);

    GlobalResponse deposit(DepositRequest depositRequest);

    GlobalResponse withdraw(WithdrawRequest withdrawRequest);
}
