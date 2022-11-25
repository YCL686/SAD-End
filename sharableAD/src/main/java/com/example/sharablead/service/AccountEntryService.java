package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.AccountEntry;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-13
 */
public interface AccountEntryService extends IService<AccountEntry> {

    GlobalResponse pageAccountEntry(long pageSize, long pageNo, Long userId);
}
