package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.SynchronizeRecord;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.enums.SynchronizeTypeEnum;
import com.example.sharablead.mapper.SynchronizeRecordMapper;
import com.example.sharablead.response.SynchronizeRecordVO;
import com.example.sharablead.service.SynchronizeRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
