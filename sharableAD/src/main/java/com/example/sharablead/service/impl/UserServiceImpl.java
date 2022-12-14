package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.constant.AppConstant;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.AccountMapper;
import com.example.sharablead.request.GenerateTokenRequest;
import com.example.sharablead.mapper.UserMapper;

import com.example.sharablead.request.LoginRequest;
import com.example.sharablead.response.*;
import com.example.sharablead.service.*;
import com.example.sharablead.util.IDUtil;
import com.example.sharablead.util.RedisUtil;
import com.example.sharablead.util.SignCheckUtil;
import com.example.sharablead.util.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * service implementation
 * </p>
 *
 * @author inncore
 * @since 2022-09-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FocusService focusService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private OpusService opusService;

    @Autowired
    private RoleService roleService;

    @Resource
    private DailyTaskConfigService dailyTaskConfigService;

    @Resource
    private DailyTaskRecordService dailyTaskRecordService;

    @Override
    public GlobalResponse login(LoginRequest loginRequest) {
        String message = loginRequest.getMessage();
        String signature = loginRequest.getSignature();
        String address = loginRequest.getAddress();
        String shortenAddress = loginRequest.getShortenAddress();
        LoginResponse response = new LoginResponse();
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getAddress, address);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        GenerateTokenRequest generateTokenRequest = new GenerateTokenRequest();
        if (!SignCheckUtil.validate(signature, address, message)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid sign");
        }
        //first login as register
        Long userId = Objects.isNull(user) ? IDUtil.nextId() : user.getId();
        if (Objects.isNull(user)) {
            user = new User();
            user.setId(userId);
            user.setAddress(address);
            user.setNickName(shortenAddress);
            user.setShortenAddress(shortenAddress);
            user.setRole(AppConstant.APP_NORMAL_MEMBER_ROLE_NAME);
            user.setStatus(UserStatusEnum.NORMAL.getCode());
            user.setGmtCreated(LocalDateTime.now());
            user.setGmtModified(LocalDateTime.now());
            userMapper.insert(user);

            Account account = new Account();
            account.setBalance(BigDecimal.ZERO);
            account.setGmtCreated(LocalDateTime.now());
            account.setGmtModified(LocalDateTime.now());
            account.setId(IDUtil.nextId());
            account.setUserId(user.getId());
            accountMapper.insert(account);

//            Role role = roleService.generateAppNormalRole(userId);
//            roleService.save(role);

        }
        generateTokenRequest.setUserId(userId);
        generateTokenRequest.setAddress(address);
        generateTokenRequest.setNickName(user.getNickName());
        List<String> roleNames = roleService.getRoleNamesByUserId(userId);
        generateTokenRequest.setRoleNames(roleNames);
        response.setToken(tokenUtil.generateToken(generateTokenRequest));
        response.setAddress(address);
        response.setNickName(user.getNickName());
        response.setUserId(userId);
        response.setAvatarUrl(user.getAvatarUrl());
        response.setRoleNames(roleNames);
        return GlobalResponse.success(response);
    }

    @Override
    public GlobalResponse logout(String token) {
        Token myToken = tokenUtil.parseToken(token);
        Long userId = myToken.getUserId();
        User user = userMapper.selectById(userId);
        if (Objects.isNull(user)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), GlobalResponseEnum.ERROR.getMessage());
        }
        redisUtil.remove(userId.toString());
        return GlobalResponse.success();
    }

    @Override
    public Map<Long, User> getUserMap(List<Long> ids) {
        Map<Long, User> map = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return map;
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(User::getId, ids);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }

        map = list.stream().collect(Collectors.toMap(User::getId, Function.identity()));
        return map;
    }

    @Override
    public GlobalResponse getProFile(boolean self, Long userId) {

        User user = userMapper.selectById(userId);
        //TODO status check
        if (Objects.isNull(user)) {
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid userId");
        }

        ProfileVO vo = new ProfileVO();
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setCharacterSign(user.getCharacterSign());
        vo.setAddress(user.getAddress());
        vo.setUserId(userId);
        vo.setSelf(self);
        vo.setGmtCreated(user.getGmtCreated());

        //liked nums
        LambdaQueryWrapper<Like> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Like::getLikedId, userId);
        vo.setLikedNum(likeService.count(lambdaQueryWrapper));

        //focused nums
        LambdaQueryWrapper<Focus> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Focus::getFocusedId, userId);
        lambdaQueryWrapper1.eq(Focus::getStatus, FocusStatusEnum.NORMAL.getCode());
        vo.setFocusedNum(focusService.count(lambdaQueryWrapper1));

        //focus nums
        LambdaQueryWrapper<Focus> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Focus::getUserId, userId);
        lambdaQueryWrapper2.eq(Focus::getStatus, FocusStatusEnum.NORMAL.getCode());
        vo.setFocusNum(focusService.count(lambdaQueryWrapper2));

        //comment nums
        LambdaQueryWrapper<Comment> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper3.eq(Comment::getFromUserId, userId);
        if (!self) {
            lambdaQueryWrapper3.eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode());
        }
        vo.setCommentNum(commentService.count(lambdaQueryWrapper3));

        //opus nums
        LambdaQueryWrapper<Opus> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper4.eq(Opus::getUserId, userId);
        lambdaQueryWrapper4.eq(Opus::getStatus, OpusStatusEnum.NORMAL.getCode());
        vo.setOpusNum(opusService.count(lambdaQueryWrapper4));

        LambdaQueryWrapper<Collect> lambdaQueryWrapper5 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper5.eq(Collect::getUserId, userId);
        lambdaQueryWrapper5.eq(Collect::getStatus, CollectStatusEnum.NORMAL.getCode());
        vo.setCollectNum(collectService.count(lambdaQueryWrapper5));

        return GlobalResponse.success(vo);
    }

    @Override
    public GlobalResponse getMentionedList(String key) {
        List<MentionedVO> list = new ArrayList<>();

        if (StringUtils.isEmpty(key)) {
            return GlobalResponse.success(list);
        }

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.likeRight(User::getNickName, key);
        lambdaQueryWrapper.eq(User::getStatus, UserStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper.select(User::getId, User::getNickName);
        List<User> list1 = userMapper.selectList(lambdaQueryWrapper);

        if (CollectionUtils.isEmpty(list1)) {
            return GlobalResponse.success(list);
        }

        list1.forEach(user -> {
            MentionedVO vo = new MentionedVO();
            BeanUtils.copyProperties(user, vo);
            list.add(vo);
        });

        return GlobalResponse.success(list);
    }

    @Override
    public GlobalResponse getDotCount(Long userId) {
        GetDotCountVO vo = new GetDotCountVO();

        LambdaQueryWrapper<DailyTaskConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(DailyTaskConfig::getId);
        lambdaQueryWrapper.eq(DailyTaskConfig::getTaskStatus, DailyTaskConfigStatusEnum.ONLINE.getCode());
        List<DailyTaskConfig> list = dailyTaskConfigService.list(lambdaQueryWrapper);
        vo.setUnfinishedDailyTaskCount(list.size());

        if (userId != 0L) {
            for (DailyTaskConfig config : list) {
                LambdaQueryWrapper<DailyTaskRecord> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(DailyTaskRecord::getTaskId, config.getId());
                lambdaQueryWrapper1.eq(DailyTaskRecord::getUserId, userId);
                lambdaQueryWrapper1.eq(DailyTaskRecord::getTaskDate, LocalDate.now());
                if (dailyTaskRecordService.count(lambdaQueryWrapper1) == 1) {
                    vo.setUnfinishedDailyTaskCount(vo.getUnfinishedDailyTaskCount() - 1);
                }
            }
            //TODO unreadMessageCount
        }

        return GlobalResponse.success(vo);
    }
}
