package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.AdAuctionRecord;
import com.example.sharablead.entity.Synchronize;
import com.example.sharablead.mapper.SynchronizeMapper;
import com.example.sharablead.response.PageSynchronizeResponse;
import com.example.sharablead.response.PageSynchronizeVO;
import com.example.sharablead.service.SynchronizeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-23
 */
@Service
public class SynchronizeServiceImpl extends ServiceImpl<SynchronizeMapper, Synchronize> implements SynchronizeService {

    @Autowired
    private SynchronizeService synchronizeService;
    @Override
    public GlobalResponse pageSynchronize(long pageNo, long pageSize) {
        LambdaQueryWrapper<Synchronize> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Synchronize::getSynchronizeDate);
        Page<Synchronize> page = new Page<>(pageNo, pageSize);
        Page<Synchronize> result = synchronizeService.page(page, lambdaQueryWrapper);

        PageSynchronizeResponse response = new PageSynchronizeResponse();
        response.setPageSize(pageSize);
        response.setPageNo(pageNo);
        response.setTotal(result.getTotal());
        response.setTotalPages(result.getPages());
        List<PageSynchronizeVO> data = new ArrayList<>();
        result.getRecords().forEach(record ->{
            PageSynchronizeVO vo = new PageSynchronizeVO();
            BeanUtils.copyProperties(record, vo);
            data.add(vo);
        });
        response.setData(data);
        return GlobalResponse.success(response);
    }
}
