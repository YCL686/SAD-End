package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Ad;
import com.example.sharablead.enums.AdStatusEnum;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.AdAuctionMapper;
import com.example.sharablead.mapper.AdMapper;
import com.example.sharablead.request.EditMyAdRequest;
import com.example.sharablead.response.MyAdVO;
import com.example.sharablead.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-17
 */
@Service
@Slf4j
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private UserService userService;

    @Override
    public GlobalResponse getAdList() {
        LambdaQueryWrapper<Ad> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Ad::getStatus, AdStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper.orderByAsc(Ad::getAdIndex);
        return GlobalResponse.success(adMapper.selectList(lambdaQueryWrapper));
    }

    @Override
    public GlobalResponse getMyAdList(Long userId) {
        LambdaQueryWrapper<Ad> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Ad::getUserId, userId);
        List<Ad> list = adMapper.selectList(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no ad");
        }
        List<MyAdVO> data = new ArrayList<>();
        for (Ad ad : list) {
            MyAdVO vo = new MyAdVO();
            BeanUtils.copyProperties(ad, vo);
            data.add(vo);
        }
        return GlobalResponse.success(data);
    }

    @Override
    public GlobalResponse editMyAd(EditMyAdRequest editMyAdRequest) {
        Long userId = editMyAdRequest.getUserId();
        Long adId = editMyAdRequest.getAdId();

        Ad ad = adMapper.selectById(adId);

        if (Objects.isNull(ad)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid ad");
        }

        if (!userId.equals(ad.getUserId())) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user");
        }

        if (ad.getEditCount() == 0) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "reach max editCount");
        }

        ad.setLink(editMyAdRequest.getLink());
        ad.setResourceUrl(editMyAdRequest.getResourceUrl());
        ad.setLabel(editMyAdRequest.getLabel());
        ad.setEditCount(ad.getEditCount() - 1);
        ad.setGmtModified(LocalDateTime.now());
        adMapper.updateById(ad);

        return GlobalResponse.success(true);
    }
}
