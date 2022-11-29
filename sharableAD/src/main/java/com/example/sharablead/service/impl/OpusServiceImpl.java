package com.example.sharablead.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.*;
import com.example.sharablead.enums.*;
import com.example.sharablead.mapper.OpusMapper;
import com.example.sharablead.request.SaveOrUpdateOpusRequest;
import com.example.sharablead.response.GetOpusByIdVO;
import com.example.sharablead.response.OpusVO;
import com.example.sharablead.response.PageProfileOpusListResponse;
import com.example.sharablead.response.PageProfileOpusVO;
import com.example.sharablead.service.*;
import com.example.sharablead.util.IDUtil;
import com.example.sharablead.util.TimeUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-10-10
 */
@Service
public class OpusServiceImpl extends ServiceImpl<OpusMapper, Opus> implements OpusService {

    @Autowired
    private OpusMapper opusMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private FocusService focusService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CollectService collectService;

    @Autowired
    private WatchService watchService;

    @Autowired
    private DailyStakingPoolService dailyStakingPoolService;

    @Autowired
    private LaunchRecordService launchRecordService;

    @Autowired
    private LaunchService launchService;

    @Override
    public GlobalResponse pageOpusList(long pageSize, long pageNo, int orderType, Long userId) {
        Page<Opus> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Opus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Opus::getStatus, OpusStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper.isNotNull(Opus::getPublishTime);
        if (orderType == OpusOrderTypeEnum.LATEST.getCode()){
            lambdaQueryWrapper.orderByDesc(Opus::getPublishTime);
        }
        //TODO
        if (orderType == OpusOrderTypeEnum.HOT.getCode()){
            lambdaQueryWrapper.orderByDesc(Opus::getHotScore);
        }
        //TODO
        if (orderType == OpusOrderTypeEnum.FOCUS.getCode()){
            if (userId == 0L){
                return GlobalResponse.error(GlobalResponseEnum.TOKEN_ERROR.getCode(), "no login");
            }
            LambdaQueryWrapper<Focus> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Focus::getUserId, userId);
            lambdaQueryWrapper1.eq(Focus::getStatus, FocusStatusEnum.NORMAL.getCode());
            lambdaQueryWrapper1.select(Focus::getFocusedId);
            List<Long> focusedIds = focusService.list(lambdaQueryWrapper1).stream().map(Focus::getFocusedId).collect(Collectors.toList());
            lambdaQueryWrapper.orderByDesc(Opus::getPublishTime);
            lambdaQueryWrapper.in(Opus::getUserId, focusedIds);
        }
        Page<Opus> result = opusMapper.selectPage(page, lambdaQueryWrapper);
        List<OpusVO> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(result.getRecords())){
            List<Long> userIds = result.getRecords().stream().map(Opus::getUserId).collect(Collectors.toList());
            Map<Long, User> map = userService.getUserMap(userIds);
                result.getRecords().forEach(opus -> {
                OpusVO vo = new OpusVO();
                BeanUtils.copyProperties(opus, vo);
                vo.setPublishTimeString(TimeUtil.getShortTime(vo.getPublishTime()));
                vo.setNickName((CollectionUtils.isEmpty(map) || !map.containsKey(vo.getUserId()))?"":map.get(vo.getUserId()).getNickName());
                vo.setAvatarUrl((CollectionUtils.isEmpty(map) || !map.containsKey(vo.getUserId()))?"":map.get(vo.getUserId()).getAvatarUrl());
                vo.setCharacterSign((CollectionUtils.isEmpty(map) || !map.containsKey(vo.getUserId()))?"":map.get(vo.getUserId()).getCharacterSign());

                LambdaQueryWrapper<Collect> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper1.eq(Collect::getOpusId, vo.getId());
                lambdaQueryWrapper1.eq(Collect::getStatus, CollectStatusEnum.NORMAL.getCode());
                vo.setCollectNum((int) collectService.count(lambdaQueryWrapper1));

                LambdaQueryWrapper<Like> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(Like::getLikedId, vo.getId());
                lambdaQueryWrapper2.eq(Like::getLikedType, LikedTypeEnum.OPUS.getCode());
                lambdaQueryWrapper2.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
                vo.setLikeNum((int) likeService.count(lambdaQueryWrapper2));

                LambdaQueryWrapper<Comment> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper3.eq(Comment::getOpusId, vo.getId());
                lambdaQueryWrapper3.eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode());
                vo.setCommentNum((int) commentService.count(lambdaQueryWrapper3));

                LambdaQueryWrapper<Watch> lambdaQueryWrapper7 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper7.eq(Watch::getWatchedId, vo.getId());
                lambdaQueryWrapper7.eq(Watch::getWatchedType, WatchedTypeEnum.OPUS.getCode());
                vo.setWatchNum((int) watchService.count(lambdaQueryWrapper7));

                LambdaQueryWrapper<DailyStakingPool> lambdaQueryWrapper8 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper8.eq(DailyStakingPool::getOpusId, vo.getId());
                lambdaQueryWrapper8.eq(DailyStakingPool::getStakingDate, LocalDate.now());
                lambdaQueryWrapper8.select(DailyStakingPool::getStakingAmount);
                DailyStakingPool pool = dailyStakingPoolService.getOne(lambdaQueryWrapper8);
                if (Objects.nonNull(pool)){
                    vo.setCurrentStakingAmount(pool.getStakingAmount());
                }

                //TODO
                if (userId != 0L){
                    LambdaQueryWrapper<Focus> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper4.eq(Focus::getUserId, userId);
                    lambdaQueryWrapper4.eq(Focus::getFocusedId, vo.getUserId());
                    lambdaQueryWrapper4.eq(Focus::getStatus, FocusStatusEnum.NORMAL.getCode());
                    if(focusService.count(lambdaQueryWrapper4) == 1){
                        vo.setFocused(true);
                    }

                    LambdaQueryWrapper<Like> lambdaQueryWrapper5 = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper5.eq(Like::getUserId, userId);
                    lambdaQueryWrapper5.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
                    lambdaQueryWrapper5.eq(Like::getLikedId, vo.getId());
                    lambdaQueryWrapper5.eq(Like::getLikedType, LikedTypeEnum.OPUS.getCode());
                    if (likeService.count(lambdaQueryWrapper5) == 1){
                        vo.setLiked(true);
                    }

                    LambdaQueryWrapper<Collect> lambdaQueryWrapper6 = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper6.eq(Collect::getStatus,CollectStatusEnum.NORMAL.getCode());
                    lambdaQueryWrapper6.eq(Collect::getUserId, userId);
                    lambdaQueryWrapper6.eq(Collect::getOpusId, vo.getId());
                    if (collectService.count(lambdaQueryWrapper6) == 1){
                        vo.setCollected(true);
                    }

                }

                if (StringUtils.isNotBlank(opus.getResourceUrl())){
                    vo.setResourceUrls(Arrays.asList(opus.getResourceUrl().split("@")));
                }
                if (StringUtils.isNotBlank(opus.getCompressResourceUrl())){
                    vo.setResourceCompressUrls(Arrays.asList(opus.getCompressResourceUrl().split("@")));
                }
                list.add(vo);
            });

            //Launch Add
            LocalDate nowDate = LocalDate.now();
            LocalDateTime now = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
            LambdaQueryWrapper<Launch> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Launch::getLaunchDate, nowDate);
            lambdaQueryWrapper1.eq(Launch::getLaunchMoment, now);
            Launch launch = launchService.getOne(lambdaQueryWrapper1);
            if (Objects.nonNull(launch)){
                Long launchId = launch.getId();
                LambdaQueryWrapper<LaunchRecord> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(LaunchRecord::getLaunchId, launchId);
                List<LaunchRecord> list1 = launchRecordService.list(lambdaQueryWrapper2);
                if (!CollectionUtils.isEmpty(list1)){
                    int count = list1.size();
                    Random rand = new Random();
                    //index is in [0, count)
                    int randomIndex = rand.nextInt(count);
                    LaunchRecord record = list1.get(randomIndex);
                    OpusVO launchVO = new OpusVO();
                    launchVO.setLaunchTitle(record.getLaunchTitle());
                    launchVO.setUserId(record.getUserId());
                    launchVO.setLaunchDescription(record.getLaunchDescription());
                    launchVO.setLaunchUrl(record.getLaunchUrl());
                    launchVO.setLaunchLink(record.getLaunchLink());
                    launchVO.setLaunchId(record.getLaunchId());
                    User user = userService.getById(record.getUserId());
                    if (Objects.nonNull(user)){
                        launchVO.setNickName(user.getNickName());
                        launchVO.setAvatarUrl(user.getAvatarUrl());
                    }
                    list.add(launchVO);
                }
            }
        }

        return GlobalResponse.success(list);

    }

    @Override
    public GlobalResponse saveOrUpdateOpus(SaveOrUpdateOpusRequest saveOrUpdateOpusRequest) {
        Long userId = saveOrUpdateOpusRequest.getUserId();
        Long opusId = saveOrUpdateOpusRequest.getId();
        Integer type = saveOrUpdateOpusRequest.getType();
        String text = saveOrUpdateOpusRequest.getText();
        if (text.length() > 200){
            saveOrUpdateOpusRequest.setSummary(text.substring(0,200) + "...");
        }else {
            saveOrUpdateOpusRequest.setSummary(text);
        }

        Opus opus = new Opus();
        BeanUtils.copyProperties(saveOrUpdateOpusRequest, opus);
        if (type == 0){
            opus.setPublishTime(LocalDateTime.now());
            opus.setStatus(OpusStatusEnum.NORMAL.getCode());
        }else {
            opus.setStatus(OpusStatusEnum.DRAFT.getCode());
        }
        opus.setHotScore(BigDecimal.ZERO);
        opus.setGmtModified(LocalDateTime.now());
        //更新操作
        if (opusId != null && opusId != 0L){
//            Opus opus1 = opusMapper.selectById(opusId);
//            if (Objects.isNull(opus1)){
//                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid operation");
//            }
//            if (!userId.equals(opus1.getUserId())){
//                return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "illegal operation");
//            }
            //opus.setGmtModified(LocalDateTime.now());
            opus.setResourceUrl("");
            opus.setCompressResourceUrl(getResourceUrl(opus.getContent()).toString());
            if (opusMapper.updateById(opus) > 0){
                return GlobalResponse.success(true);
            }
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "error");
        }

        opus.setId(IDUtil.nextId());
        opus.setGmtCreated(LocalDateTime.now());
        opus.setResourceUrl(getResourceUrl(opus.getContent()).get("origin"));
        opus.setCompressResourceUrl(getResourceUrl(opus.getContent()).get("compress"));

        if (opusMapper.insert(opus) > 0){
            return GlobalResponse.success(true);
        }
        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "error");
    }

    @Override
    public GlobalResponse getOpusById(Long opusId, Long userId) {
        //TODO status check
        //opusId不存在
        Opus opus = opusMapper.selectById(opusId);
        if (Objects.isNull(opus)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid id");
        }

        // 该opus非正常状态
        if (OpusStatusEnum.NORMAL.getCode() != opus.getStatus()){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid opus status");
        }

        //opusId对应的作者不存在
        Long userId1 = opus.getUserId();
        User user = userService.getById(userId1);
        if (Objects.isNull(user)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid user");
        }
        GetOpusByIdVO vo = new GetOpusByIdVO();
        BeanUtils.copyProperties(opus, vo);
        vo.setCharacterSign(user.getCharacterSign());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setNickName(user.getNickName());

        LambdaQueryWrapper<Like> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper.eq(Like::getLikedId, opusId);
        lambdaQueryWrapper.eq(Like::getLikedType, LikedTypeEnum.OPUS.getCode());
        vo.setLikeNum((int) likeService.count(lambdaQueryWrapper));

        LambdaQueryWrapper<Collect> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Collect::getStatus, CollectStatusEnum.NORMAL.getCode());
        lambdaQueryWrapper1.eq(Collect::getOpusId, opusId);
        vo.setCollectNum((int) collectService.count(lambdaQueryWrapper1));

        LambdaQueryWrapper<Comment> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Comment::getOpusId, opusId);
        lambdaQueryWrapper2.eq(Comment::getStatus, CommentStatusEnum.NORMAL.getCode());
        vo.setCommentNum((int) commentService.count(lambdaQueryWrapper2));

        LambdaQueryWrapper<Watch> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper3.eq(Watch::getWatchedId, opusId);
        lambdaQueryWrapper3.eq(Watch::getWatchedType, WatchedTypeEnum.OPUS.getCode());
        vo.setWatchNum((int) watchService.count(lambdaQueryWrapper3));

        //说明用户是登录状态访问 判断点赞 关注 收藏状态
        if (userId != 0L){
            if (userId.equals(opus.getUserId())){
                vo.setSelf(true);
            }
            LambdaQueryWrapper<Like> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper4.eq(Like::getUserId, userId);
            lambdaQueryWrapper4.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
            lambdaQueryWrapper4.eq(Like::getLikedId, opusId);
            lambdaQueryWrapper4.eq(Like::getLikedType, LikedTypeEnum.OPUS.getCode());
            vo.setLiked(likeService.count(lambdaQueryWrapper4) == 1);

            LambdaQueryWrapper<Collect> lambdaQueryWrapper5 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper5.eq(Collect::getOpusId, opusId);
            lambdaQueryWrapper5.eq(Collect::getUserId, userId);
            lambdaQueryWrapper5.eq(Collect::getStatus, CollectStatusEnum.NORMAL.getCode());
            vo.setCollected(collectService.count(lambdaQueryWrapper5) == 1);

            LambdaQueryWrapper<Focus> lambdaQueryWrapper6 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper6.eq(Focus::getUserId, userId);
            lambdaQueryWrapper6.eq(Focus::getFocusedId, opus.getUserId());
            lambdaQueryWrapper6.eq(Focus::getStatus, FocusStatusEnum.NORMAL.getCode());
            vo.setFocused(focusService.count(lambdaQueryWrapper6) == 1);
        }
        return GlobalResponse.success(vo);
    }

    @Override
    public GlobalResponse pageProfileOpusList(Boolean self, Integer status, long pageNo, long pageSize, Long userId) {
        Page<Opus> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<Opus> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Opus::getId, Opus::getTitle, Opus::getSummary, Opus::getResourceUrl, Opus::getGmtCreated, Opus::getGmtModified, Opus::getUserId, Opus::getMinted);
        lambdaQueryWrapper.eq(Opus::getUserId, userId);
        if (self){ //本人情况下 status才有查询意义
            lambdaQueryWrapper.eq(Opus::getStatus, status);
        }else {//非本人只展示normal状态的作品
            lambdaQueryWrapper.eq(Opus::getStatus, OpusStatusEnum.NORMAL.getCode());
        }

        Page<Opus> result = opusMapper.selectPage(page, lambdaQueryWrapper);
        PageProfileOpusListResponse response = new PageProfileOpusListResponse();
        List<PageProfileOpusVO> list = new ArrayList<>();
        response.setSelf(self);
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotal(result.getTotal());
        response.setTotalPages(result.getPages());

        if (CollectionUtils.isEmpty(result.getRecords())){
            response.setList(list);
            return GlobalResponse.success(response);
        }

        result.getRecords().forEach(record->{
            PageProfileOpusVO vo = new PageProfileOpusVO();
            BeanUtils.copyProperties(record, vo);
            if (StringUtils.isNotBlank(record.getResourceUrl())){
                vo.setResourceUrls(Arrays.asList(record.getResourceUrl().split("@")));
            }
            list.add(vo);
        });
        response.setList(list);

        return GlobalResponse.success(response);
    }

    @Override
    public GlobalResponse getOpusByIdForPublish(Long opusId, Long userId) {
        Opus opus = opusMapper.selectById(opusId);

        if (Objects.isNull(opus)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid opusId");
        }

        if (!opus.getUserId().equals(userId)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid userId");
        }
        //状态 待审核暂不允许编辑 TODO 后面考虑minted状态
        if (OpusStatusEnum.WAIT_AUDIT.getCode() == opus.getStatus()){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid opus status");
        }
        Map<String, String> map = new HashMap<>();
        map.put("title", opus.getTitle());
        map.put("content", opus.getContent());
        map.put("resourceUrl", opus.getResourceUrl());
        map.put("opusId", opus.getId().toString());
        return GlobalResponse.success(map);
    }

    public Map<String, String> getResourceUrl(String content){
        Map<String, String> map = new HashMap<>();
        Document document = Jsoup.parse(content);
        Elements e = document.getElementsByTag("img");
        //Elements e1 = document.getElementsByTag("video");
        StringBuilder resourceUrl = new StringBuilder();
        for (Element element : e) {
            resourceUrl.append(element.attr("src")).append("@");
        }

//        for (Element element : e1) {
//            resourceUrl.append(element.attr("src")).append("@");
//        }

        map.put("origin", resourceUrl.toString());
        map.put("compress", resourceUrl.toString().replaceAll("origin", "compress"));

        return map;
    }
}
