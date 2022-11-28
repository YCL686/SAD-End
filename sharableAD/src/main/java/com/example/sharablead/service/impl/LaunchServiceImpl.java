package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Launch;
import com.example.sharablead.entity.LaunchRecord;
import com.example.sharablead.entity.User;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.mapper.LaunchMapper;
import com.example.sharablead.response.LaunchVO;
import com.example.sharablead.response.LauncherVO;
import com.example.sharablead.service.LaunchRecordService;
import com.example.sharablead.service.LaunchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-28
 */
@Service
public class LaunchServiceImpl extends ServiceImpl<LaunchMapper, Launch> implements LaunchService {

    @Autowired
    private LaunchService launchService;

    @Autowired
    private LaunchRecordService launchRecordService;

    @Autowired
    private UserService userService;

    @Override
    public GlobalResponse getLaunchList() {
        LocalDate now = LocalDate.now();
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime nowMoment = nowTime.withMinute(0).withSecond(0).withNano(0);
        Integer nowIndex = nowTime.getHour();

        LambdaQueryWrapper<Launch> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Launch::getLaunchDate, now);
        lambdaQueryWrapper.orderByAsc(Launch::getLaunchIndex);

        List<Launch> list = launchService.list(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no data");
        }

        List<LaunchVO> result = new ArrayList<>();

        for(Launch launch : list){
            Long launchId = launch.getId();
            LaunchVO vo = new LaunchVO();
            vo.setLaunchCount(launch.getLaunchCount());
            vo.setLaunchIndex(launch.getLaunchIndex());
            vo.setLaunchPrice(launch.getLaunchPrice());
            vo.setLaunchMoment(launch.getLaunchMoment());
            vo.setLaunchTime(launch.getLaunchMoment().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            vo.setLaunchId(launchId);

            if (nowIndex <= launch.getLaunchIndex()){
                vo.setLaunchable(true);
            }

            LambdaQueryWrapper<LaunchRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(LaunchRecord::getLaunchId, launchId);
            List<LaunchRecord> records = launchRecordService.list(lambdaQueryWrapper1);

            if (!CollectionUtils.isEmpty(records)){
                List<Long> userIds = records.stream().map(LaunchRecord::getUserId).collect(Collectors.toList());
                Map<Long, User> userMap = userService.getUserMap(userIds);
                List<LauncherVO> temp = new ArrayList<>();
                for (LaunchRecord record : records){
                    LauncherVO launcherVO = new LauncherVO();
                    launcherVO.setUserId(record.getUserId());
                    launcherVO.setNickName(userMap.containsKey(record.getUserId())? userMap.get(record.getUserId()).getNickName() : "");
                    launcherVO.setAvatarUrl(userMap.containsKey(record.getUserId())? userMap.get(record.getUserId()).getAvatarUrl() : "");
                    temp.add(launcherVO);
                }
                vo.setData(temp);
            }
            result.add(vo);
        }

        return GlobalResponse.success(result);
    }
}
