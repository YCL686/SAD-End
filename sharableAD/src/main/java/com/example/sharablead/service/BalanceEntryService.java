package com.example.sharablead.service;

import com.example.sharablead.request.ChangeBalanceEntryRequest;

public interface BalanceEntryService {

    boolean changeBalanceEntry(ChangeBalanceEntryRequest request);
}
