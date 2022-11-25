package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.DailyTaskConfigMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.response.GetDailyTaskVO;
import com.example.sharablead.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-04
 */
@Service
public class DailyTaskConfigServiceImpl extends ServiceImpl<DailyTaskConfigMapper, DailyTaskConfig> implements DailyTaskConfigService {

    @Autowired
    private DailyTaskConfigMapper dailyTaskConfigMapper;

    @Autowired
    private DailyTaskRecordService dailyTaskRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OpusService opusService;

    @Override
    public GlobalResponse getDailyTask(Long userId) {
        Boolean logged = false;
        LambdaQueryWrapper<DailyTaskConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DailyTaskConfig::getTaskStatus, DailyTaskConfigStatusEnum.ONLINE.getCode());
        List<DailyTaskConfig> list = dailyTaskConfigMapper.selectList(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "no online daily task");
        }
        if (userId != 0L){
            User user = userService.getById(userId);
            if (!Objects.isNull(user) && user.getStatus() == UserStatusEnum.NORMAL.getCode()){
                logged = true;
            }
        }


        List<GetDailyTaskVO> results = new ArrayList<>();

        for(DailyTaskConfig config : list){
            GetDailyTaskVO vo = new GetDailyTaskVO();
            vo.setTaskId(config.getId());
            vo.setTaskCount(config.getTaskCount());
            vo.setTaskName(config.getTaskName());
            vo.setTaskReward(config.getTaskReward());
            if (logged){
                int counted = (int) calculateTaskCounted(config.getTaskKey(), userId);
                vo.setTaskCounted(counted > vo.getTaskCount()? vo.getTaskCount() : counted);
                if (vo.getTaskCounted() >= vo.getTaskCount()){
                    vo.setFinished(true);
                }
                LambdaQueryWrapper<DailyTaskRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(DailyTaskRecord::getTaskId, config.getId());
                lambdaQueryWrapper1.eq(DailyTaskRecord::getUserId, userId);
                lambdaQueryWrapper1.eq(DailyTaskRecord::getTaskDate, LocalDate.now());
                vo.setGotten(dailyTaskRecordService.count(lambdaQueryWrapper1) == 1);
            }

            results.add(vo);
        }

        return GlobalResponse.success(results);
    }
    //taskKey is made to config new task; i want it much more smart, but it seems difficult
    private long calculateTaskCounted(String taskKey, Long userId){
        LocalDateTime zeroTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime nowTime = LocalDateTime.now();
        if ("like".equals(taskKey)){
            LambdaQueryWrapper<Like> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Like::getUserId, userId);
            lambdaQueryWrapper.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
            lambdaQueryWrapper.between(Like::getGmtCreated, zeroTime, nowTime);
            return likeService.count(lambdaQueryWrapper);
        }

        if ("comment".equals(taskKey)){
            LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getFromUserId, userId);
            lambdaQueryWrapper.eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode());
            lambdaQueryWrapper.between(Comment::getGmtCreated, zeroTime, nowTime);
            return commentService.count(lambdaQueryWrapper);

        }

        if ("opus".equals(taskKey)){
            LambdaQueryWrapper<Opus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Opus::getUserId, userId);
            lambdaQueryWrapper.eq(Opus::getStatus, OpusStatusEnum.NORMAL.getCode());
            lambdaQueryWrapper.between(Opus::getPublishTime, zeroTime, nowTime);
            return opusService.count(lambdaQueryWrapper);
        }

        if ("staking".equals(taskKey)){
            //TODO
        }

        if ("auction".equals(taskKey)){
            //TODO
        }

        return 0;
    }
}
