package com.example.sharablead.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Like;
import com.example.sharablead.request.OperateLikeRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-20
 */
public interface LikeService extends IService<Like> {

    GlobalResponse operateLike(OperateLikeRequest operateLikeRequest);
}
