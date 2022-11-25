package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.DailyStakingPoolStatusEnum;
import com.example.sharablead.enums.DailyStakingRecordStatusEnum;
import com.example.sharablead.mapper.DailyStakingPoolMapper;
import com.example.sharablead.response.DailyStakingPoolVO;
import com.example.sharablead.response.DailyStakingRecordVO;
import com.example.sharablead.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-11-07
 */
@Service
public class DailyStakingPoolServiceImpl extends ServiceImpl<DailyStakingPoolMapper, DailyStakingPool> implements DailyStakingPoolService {

    @Autowired
    private DailyStakingPoolMapper dailyStakingPoolMapper;

    @Autowired
    private DailyStakingRecordService dailyStakingRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private OpusService opusService;

    @Value("${daily-staking.single-opus-staking-numbers}")
    private Integer totalNum;

    @Override
    public GlobalResponse getDailyStakingPool(Long opusId, LocalDate stakingDate, Long userId) {
        DailyStakingPoolVO vo = new DailyStakingPoolVO();
        Duration duration = Duration.between(LocalDateTime.now(),LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
        vo.setDeadLine(duration.toMillis());
        vo.setTotalNum(totalNum);

        LambdaQueryWrapper<DailyStakingPool> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DailyStakingPool::getOpusId, opusId);
        lambdaQueryWrapper.eq(DailyStakingPool::getStakingDate, stakingDate);

        DailyStakingPool dailyStakingPool = dailyStakingPoolMapper.selectOne(lambdaQueryWrapper);

        Opus opus = opusService.getById(opusId);
        if (Objects.nonNull(opus) && Objects.nonNull(opus.getHotScore())){
            vo.setHotScore(opus.getHotScore());
            LambdaQueryWrapper<Opus> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.ge(Opus::getHotScore, opus.getHotScore());
            vo.setCurrentRank(opusService.count(lambdaQueryWrapper1));
        }

        if (userId != 0L ){
            LambdaQueryWrapper<Account> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Account::getUserId, userId);
            Account account = accountService.getOne(lambdaQueryWrapper1);
            if (Objects.nonNull(account)){
                vo.setOffChainToken(account.getBalance());
            }
        }

        //calculate totalAmount
        LambdaQueryWrapper<DailyStakingPool> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(DailyStakingPool::getStakingDate, stakingDate);
        lambdaQueryWrapper2.select(DailyStakingPool::getStakingAmount);
        List<DailyStakingPool> list = dailyStakingPoolMapper.selectList(lambdaQueryWrapper2);
        if (!CollectionUtils.isEmpty(list)){
            for (DailyStakingPool pool : list){
                vo.setTotalAmount(vo.getTotalAmount().add(pool.getStakingAmount()));
            }
        }

        if (Objects.nonNull(dailyStakingPool)) {
            Long poolId = dailyStakingPool.getId();
            vo.setStakingAmount(dailyStakingPool.getStakingAmount());
            vo.setStatus(dailyStakingPool.getStatus());
            vo.setStatusName(DailyStakingPoolStatusEnum.getName(vo.getStatus()));
            if (vo.getStatus() != DailyStakingPoolStatusEnum.STAKABLE.getCode()) {
                vo.setStakable(false);
            }

            //boolean staked
            if (userId != 0L && vo.getStakable()) {
                LambdaQueryWrapper<DailyStakingRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(DailyStakingRecord::getPoolId, poolId);
                lambdaQueryWrapper1.eq(DailyStakingRecord::getUserId, userId);
                vo.setStakable(dailyStakingRecordService.count(lambdaQueryWrapper1) == 0);
            }

            //构造DailyStakingRecordVO
            List<DailyStakingRecordVO> results = new ArrayList<>();
            LambdaQueryWrapper<DailyStakingRecord> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper3.eq(DailyStakingRecord::getPoolId, poolId);
            List<DailyStakingRecord> list1 = dailyStakingRecordService.list(lambdaQueryWrapper3);

            if (CollectionUtils.isEmpty(list1)) {
                vo.setList(results);
                return GlobalResponse.success(vo);
            }
            List<Long> userIds = list1.stream().map(DailyStakingRecord::getUserId).collect(Collectors.toList());
            Map<Long, User> userMap = userService.getUserMap(userIds);
            for (DailyStakingRecord record : list1) {
                DailyStakingRecordVO vo1 = new DailyStakingRecordVO();
                vo1.setStakingAmount(record.getStakingAmount());
                vo1.setUserId(record.getUserId());
                vo1.setStatus(record.getStatus());
                vo1.setStatusName(DailyStakingRecordStatusEnum.getName(record.getStatus()));
                User user = userMap.getOrDefault(record.getUserId(), null);
                vo1.setNickName(Objects.isNull(user) ? "" : user.getNickName());
                vo1.setAvatarUrl(Objects.isNull(user) ? "" : user.getAvatarUrl());
                vo1.setStakingTime(record.getGmtCreated());
                results.add(vo1);
            }
            vo.setStakedNum(list1.size());
            vo.setList(results);
        }

        return GlobalResponse.success(vo);
    }
}
