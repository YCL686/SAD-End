package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Collect;
import com.example.sharablead.enums.CollectStatusEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.CollectMapper;
import com.example.sharablead.request.OperateCollectRequest;
import com.example.sharablead.service.CollectService;
import com.example.sharablead.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-10-20
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public GlobalResponse operateCollect(OperateCollectRequest operateCollectRequest) {
        Long userId = operateCollectRequest.getUserId();
        Long opusId = operateCollectRequest.getOpusId();

        LambdaQueryWrapper<Collect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collect::getUserId, userId);
        lambdaQueryWrapper.eq(Collect::getOpusId, opusId);
        lambdaQueryWrapper.select(Collect::getId, Collect::getStatus);
        Collect collect = collectMapper.selectOne(lambdaQueryWrapper);

        if (Objects.isNull(collect)){
            collect = new Collect();
            collect.setId(IDUtil.nextId());
            collect.setStatus(CollectStatusEnum.NORMAL.getCode());
            collect.setUserId(userId);
            collect.setOpusId(opusId);
            collect.setGmtCreated(LocalDateTime.now());
            collect.setGmtModified(LocalDateTime.now());
            if(collectMapper.insert(collect) > 0){
                return GlobalResponse.success(true);
            }
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "insert fail");
        }

        collect.setGmtModified(LocalDateTime.now());
        if (collect.getStatus() == CollectStatusEnum.NORMAL.getCode()){
            collect.setStatus(CollectStatusEnum.CANCEL.getCode());
        }else {
            if (collect.getStatus() == CollectStatusEnum.CANCEL.getCode()){
                collect.setStatus(CollectStatusEnum.NORMAL.getCode());
            }
        }

        if (collectMapper.updateById(collect) > 0){
            return GlobalResponse.success(true);
        }

        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "update fail");
    }
}
