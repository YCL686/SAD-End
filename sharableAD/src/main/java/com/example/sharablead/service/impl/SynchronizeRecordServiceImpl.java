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

        GetSynchronizeInfoVO vo = new GetSynchronizeInfoVO();
        for (SynchronizeRecord record : list){
            int type = record.getSynchronizeType();

            vo.setBurnAmount(vo.getBurnAmount().add(record.getBurnAmount()));
            vo.setOnSaleAmount(vo.getOnSaleAmount().add(record.getOnSaleAmount()));
            if (type == SynchronizeTypeEnum.DAILY_TASK.getCode()){
                vo.setFeedBackAmountMinus(vo.getFeedBackAmountMinus().add(record.getFeedBackAmount()));
            }else {
                vo.setFeedBackAmountPlus(vo.getFeedBackAmountPlus().add(record.getFeedBackAmount()));
            }
        }
        return null;
    }
}
