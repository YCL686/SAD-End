package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Synchronize;
import com.example.sharablead.entity.SynchronizeRecord;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.enums.SynchronizeTypeEnum;
import com.example.sharablead.mapper.SynchronizeRecordMapper;
import com.example.sharablead.response.GetSynchronizeInfoVO;
import com.example.sharablead.response.SynchronizeRecordVO;
import com.example.sharablead.service.SynchronizeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.SynchronizeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-23
 */
@Service
public class SynchronizeRecordServiceImpl extends ServiceImpl<SynchronizeRecordMapper, SynchronizeRecord> implements SynchronizeRecordService {

    @Autowired
    private SynchronizeRecordService synchronizeRecordService;

    @Autowired
    private SynchronizeService synchronizeService;

    @Override
    public GlobalResponse getSynchronizeRecordList(Long synchronizeId) {

        if (Objects.isNull(synchronizeId) || synchronizeId == 0L){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid synchronizeId");
        }

        List<SynchronizeRecordVO> data = new ArrayList<>();

        LambdaQueryWrapper<SynchronizeRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SynchronizeRecord::getSynchronizeId, synchronizeId);
        lambdaQueryWrapper.orderByDesc(SynchronizeRecord::getSynchronizeType);
        List<SynchronizeRecord> list = synchronizeRecordService.list(lambdaQueryWrapper);
        list.forEach(record->{
            SynchronizeRecordVO vo = new SynchronizeRecordVO();
            BeanUtils.copyProperties(record, vo);
            vo.setSynchronizeTypeName(SynchronizeTypeEnum.getName(vo.getSynchronizeType()));
            data.add(vo);
        });
        return GlobalResponse.success(data);
    }

    @Override
    public GlobalResponse getSynchronizeInfo() {

        LocalDate now = LocalDate.now().minusDays(1);
        LambdaQueryWrapper<Synchronize> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Synchronize::getSynchronizeDate, now);
        Synchronize synchronize = synchronizeService.getOne(lambdaQueryWrapper);

        if (Objects.isNull(synchronize)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no data");
        }

        Long synchronizeId = synchronize.getId();
        LambdaQueryWrapper<SynchronizeRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(SynchronizeRecord::getSynchronizeId, synchronizeId);
        List<SynchronizeRecord> list = synchronizeRecordService.list(lambdaQueryWrapper1);

        if (CollectionUtils.isEmpty(list)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no data");
        }

        LambdaQueryWrapper<Synchronize> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Synchronize::getSynchronizeDate, now.minusDays(1));
        Synchronize synchronize1 = synchronizeService.getOne(lambdaQueryWrapper2);
        Long LastSynchronizeId = Objects.isNull(synchronize1)? 0L : synchronize1.getId();

        BigDecimal synchronizeAmount = BigDecimal.ZERO;
        BigDecimal burnAmount = BigDecimal.ZERO;
        BigDecimal onSaleAmount = BigDecimal.ZERO;
        BigDecimal feedBackAmount = BigDecimal.ZERO;

        BigDecimal lastSynchronizeAmount = BigDecimal.ZERO;
        BigDecimal lastBurnAmount = BigDecimal.ZERO;
        BigDecimal lastOnSaleAmount = BigDecimal.ZERO;
        BigDecimal lastFeedBackAmount = BigDecimal.ZERO;

        GetSynchronizeInfoVO vo = new GetSynchronizeInfoVO();
        for (SynchronizeRecord record : list){
            synchronizeAmount = synchronizeAmount.add(record.getSynchronizeAmount());
            burnAmount = burnAmount.add(record.getBurnAmount());
            onSaleAmount = onSaleAmount.add(record.getOnSaleAmount());
            feedBackAmount = feedBackAmount.add(record.getFeedBackAmount());
        }

        LambdaQueryWrapper<SynchronizeRecord> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper3.eq(SynchronizeRecord::getSynchronizeId, LastSynchronizeId);
        List<SynchronizeRecord> lastList = synchronizeRecordService.list(lambdaQueryWrapper3);

        if (!CollectionUtils.isEmpty(lastList)){
            for (SynchronizeRecord record : lastList){
                lastSynchronizeAmount = lastSynchronizeAmount.add(record.getSynchronizeAmount());
                lastBurnAmount = lastBurnAmount.add(record.getBurnAmount());
                lastOnSaleAmount = lastOnSaleAmount.add(record.getOnSaleAmount());
                lastFeedBackAmount = lastFeedBackAmount.add(record.getFeedBackAmount());
            }
        }

        vo.setSynchronizeAmount(synchronizeAmount);
        vo.setFeedBackAmount(feedBackAmount);
        vo.setBurnAmount(burnAmount);
        vo.setOnSaleAmount(onSaleAmount);

        if (lastSynchronizeAmount.compareTo(BigDecimal.ZERO) > 0){
            vo.setSynchronizeAmountGrowthRate((synchronizeAmount.subtract(lastSynchronizeAmount)).multiply(BigDecimal.valueOf(100)).divide(lastSynchronizeAmount, 2, RoundingMode.HALF_UP));
        }

        if (lastBurnAmount.compareTo(BigDecimal.ZERO) > 0){
            vo.setBurnAmountGrowthRate((burnAmount.subtract(lastBurnAmount)).multiply(BigDecimal.valueOf(100)).divide(lastBurnAmount, 2, RoundingMode.HALF_UP));
        }

        if (lastFeedBackAmount.compareTo(BigDecimal.ZERO) > 0){
            vo.setFeedBackAmountGrowthRate((feedBackAmount.subtract(lastFeedBackAmount)).multiply(BigDecimal.valueOf(100)).divide(lastFeedBackAmount, 2, RoundingMode.HALF_UP));
        }

        if (lastOnSaleAmount.compareTo(BigDecimal.ZERO) > 0){
            vo.setOnSaleAmountGrowthRate((onSaleAmount.subtract(lastOnSaleAmount)).multiply(BigDecimal.valueOf(100)).divide(lastOnSaleAmount, 2, RoundingMode.HALF_UP));
        }
        return GlobalResponse.success(vo);
    }
}
