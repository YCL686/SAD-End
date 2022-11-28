package com.example.sharablead.service;

/**
 * @Author YCL686
 * @Date 2022/10/23
 */
public interface SpecificCronJobService {
    void calculateActiveScore();

    void calculateHotScore();

    void calculateDailyTaskReward();

    void synchronizeWallet();

    void processWithdrawRequest();

    void processAdAuctionBusiness();

    void launchStart();
}
