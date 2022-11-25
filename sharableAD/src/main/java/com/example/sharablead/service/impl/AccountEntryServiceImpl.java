package com.example.sharablead.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Account;
import com.example.sharablead.entity.AccountEntry;
import com.example.sharablead.enums.AccountEntryEventEnum;
import com.example.sharablead.enums.AccountEntryStatusEnum;
import com.example.sharablead.enums.AccountEntryTypeEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.AccountEntryMapper;
import com.example.sharablead.mapper.AccountMapper;
import com.example.sharablead.response.AccountEntryVO;
import com.example.sharablead.response.PageAccountEntryListResponse;
import com.example.sharablead.service.AccountEntryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
public class AccountEntryServiceImpl extends ServiceImpl<AccountEntryMapper, AccountEntry> implements AccountEntryService {

    @Autowired
    private AccountEntryMapper accountEntryMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public GlobalResponse pageAccountEntry(long pageSize, long pageNo, Long userId) {
        LambdaQueryWrapper<Account> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Account::getUserId, userId);
        lambdaQueryWrapper.select(Account::getId);

        Account account = accountMapper.selectOne(lambdaQueryWrapper);
        if (Objects.isNull(account)){
            log.error("account does not exist, userId = {}", userId);
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "account does not exist");
        }
        Long accountId = account.getId();
        LambdaQueryWrapper<AccountEntry> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(AccountEntry::getAccountId, accountId);
        lambdaQueryWrapper1.orderByDesc(AccountEntry::getGmtCreated);

        Page<AccountEntry> page = new Page<>(pageNo, pageSize);
        Page<AccountEntry> result = accountEntryMapper.selectPage(page, lambdaQueryWrapper1);

        if (CollectionUtil.isEmpty(result.getRecords())){
            GlobalResponse.success("no data");
        }

        PageAccountEntryListResponse response = new PageAccountEntryListResponse();
        List<AccountEntryVO> list = new ArrayList<>();
        response.setCurrentPageNo(result.getCurrent());
        response.setPageSize(pageSize);
        response.setTotal(result.getTotal());
        response.setTotalPageNos(result.getPages());
        result.getRecords().stream().forEach( record ->{
            AccountEntryVO vo = new AccountEntryVO();
            BeanUtils.copyProperties(record, vo);
            vo.setEntryEventName(AccountEntryEventEnum.getName(vo.getEntryEvent()));
            vo.setEntryTypeName(AccountEntryTypeEnum.getName(vo.getEntryType()));
            vo.setStatusName(AccountEntryStatusEnum.getName(vo.getStatus()));
            list.add(vo);
        });
        response.setList(list);
        return GlobalResponse.success(response);
    }
}
