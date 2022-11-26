package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.SynchronizeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-11-23
 */
public interface SynchronizeRecordService extends IService<SynchronizeRecord> {

    GlobalResponse getSynchronizeRecordList(Long synchronizeId);

    GlobalResponse getSynchronizeInfo();
}
