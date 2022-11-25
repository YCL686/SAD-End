package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Like;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.enums.LikeStatusEnum;
import com.example.sharablead.mapper.LikeMapper;
import com.example.sharablead.request.OperateLikeRequest;
import com.example.sharablead.service.LikeService;
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
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public GlobalResponse operateLike(OperateLikeRequest operateLikeRequest) {
        Long userId = operateLikeRequest.getUserId();
        Long likedId = operateLikeRequest.getLikedId();
        Integer likedType = operateLikeRequest.getLikedType();

        LambdaQueryWrapper<Like> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Like::getUserId, userId);
        lambdaQueryWrapper.eq(Like::getLikedId, likedId);
        lambdaQueryWrapper.eq(Like::getLikedType, likedType);
        lambdaQueryWrapper.select(Like::getId, Like::getStatus);

        Like like = likeMapper.selectOne(lambdaQueryWrapper);

        if (Objects.isNull(like)){
            like = new Like();
            like.setId(IDUtil.nextId());
            like.setLikedId(likedId);
            like.setUserId(userId);
            like.setLikedType(likedType);
            like.setStatus(LikeStatusEnum.NORMAL.getCode());
            like.setGmtCreated(LocalDateTime.now());
            like.setGmtModified(LocalDateTime.now());

            if (likeMapper.insert(like) > 0){
                //TODO messageProducerService.send(userId, messageTemplateId, etc...)
                return GlobalResponse.success(true);
            }

            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "insert failed");
        }

        like.setGmtModified(LocalDateTime.now());
        if (LikeStatusEnum.NORMAL.getCode() == like.getStatus()){
            like.setStatus(LikeStatusEnum.CANCEL.getCode());
        }else {
            if (LikeStatusEnum.CANCEL.getCode() == like.getStatus()){
                like.setStatus(LikeStatusEnum.NORMAL.getCode());
            }
        }

        if (likeMapper.updateById(like) > 0){
            return GlobalResponse.success(true);
        }

        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "update fail");
    }
}
