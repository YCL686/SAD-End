package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;

import java.math.BigDecimal;

public interface Web3jService {

    GlobalResponse withdraw(String toAddress, BigDecimal amount);

    GlobalResponse synchronize(String fromAddress, String toAddress, BigDecimal amount);

    GlobalResponse getBalanceOf(String fromAddress);
}
