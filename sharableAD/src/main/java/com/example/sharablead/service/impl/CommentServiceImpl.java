package com.example.sharablead.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Comment;
import com.example.sharablead.entity.Like;
import com.example.sharablead.entity.Opus;
import com.example.sharablead.entity.User;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.enums.LikeStatusEnum;
import com.example.sharablead.enums.LikedTypeEnum;
import com.example.sharablead.mapper.CommentMapper;
import com.example.sharablead.request.AddCommentRequest;
import com.example.sharablead.response.CommentVO;
import com.example.sharablead.response.PageCommentListResponse;
import com.example.sharablead.service.*;
import com.example.sharablead.util.IDUtil;
import com.example.sharablead.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author inncore
 * @since 2022-10-18
 */
@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OpusService opusService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ContentService contentService;

    @Override
    public GlobalResponse pageCommentList(long pageNo, long subPageNo, long opusId, int orderType, boolean showAllComments, boolean showAllSubComments, long subCommentParentId, Long userId) {
        PageCommentListResponse response = new PageCommentListResponse();
        List<Comment> temp = new ArrayList<>();
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getOpusId, opusId);
        lambdaQueryWrapper.isNull(Comment::getParentId);
        lambdaQueryWrapper.orderByDesc(Comment::getGmtCreated);
        //是否展示全部评论
            Page<Comment> page = new Page<>(pageNo, 5);
            // TODO if (orderType == 0) 排序
            Page<Comment> result = commentMapper.selectPage(page, lambdaQueryWrapper);
            temp = result.getRecords();
            long total = result.getTotal();
            if (!showAllComments){
                if (total > 5) {
                    response.setRemainCommentNum((int) (total - 5));
                }
            }else {
                lambdaQueryWrapper.last("limit 5, " + total);
                temp = commentMapper.selectList(lambdaQueryWrapper);
            }
        if (CollectionUtils.isEmpty(temp)){
            return GlobalResponse.success("no data");
        }


        Set<Long> userIds = new HashSet<>();
        temp.forEach(record->{
            userIds.add(record.getFromUserId());
            userIds.add(record.getToUserId());
        });

        Map<Long, User> map = userService.getUserMap(new ArrayList<>(userIds));
        List<CommentVO> list = new ArrayList<>();
        temp.forEach(record->{
            Long fromUserId = record.getFromUserId();
            Long toUserId = record.getToUserId();
            CommentVO commentVO = new CommentVO();
            BeanUtils.copyProperties(record, commentVO);

            commentVO.setGmtCreatedString(TimeUtil.getShortTime(commentVO.getGmtCreated()));

            //From Info
            if (map.containsKey(fromUserId)){
                commentVO.setFromNickName(map.get(fromUserId).getNickName());
                commentVO.setFromAvatarUrl(map.get(fromUserId).getAvatarUrl());
                commentVO.setFromUserId(fromUserId);
            }
            //To Info
            if (map.containsKey(toUserId)){
                commentVO.setToNickName(map.get(toUserId).getNickName());
                commentVO.setToAvatarUrl(map.get(toUserId).getAvatarUrl());
                commentVO.setToUserId(toUserId);
            }
            //评论点赞数
            LambdaQueryWrapper<Like> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Like::getLikedType, LikedTypeEnum.COMMENT.getCode());
            lambdaQueryWrapper1.eq(Like::getLikedId, commentVO.getId());
            commentVO.setLikedNum((int) likeService.count(lambdaQueryWrapper1));

            //登录状态下访问 判断有没有点赞
            if (userId != 0L){
                LambdaQueryWrapper<Like> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
                lambdaQueryWrapper2.eq(Like::getUserId, userId);
                lambdaQueryWrapper2.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
                lambdaQueryWrapper2.eq(Like::getLikedId, commentVO.getId());
                commentVO.setLiked(likeService.count(lambdaQueryWrapper2) > 0);
            }

            //generate sub comment
            List<Comment> list1 = new ArrayList<>();
            LambdaQueryWrapper<Comment> lambdaQueryWrapper2 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper2.eq(Comment::getOpusId, opusId);
            lambdaQueryWrapper2.eq(Comment::getParentId, record.getId());
                Page<Comment> page1 = new Page<>(subPageNo, 5);
                Page<Comment> result1 = commentMapper.selectPage(page1, lambdaQueryWrapper2);
                long total1 = result1.getTotal();
                if (!showAllSubComments){
                    if (total1 > 5) {
                        commentVO.setRemainCommentNum((int) (total1 - 5));
                    }
                    list1 = result1.getRecords();
                }else {
                    if (subCommentParentId == record.getId()){
                        lambdaQueryWrapper2.last("limit 5, " + total1);
                        list1 = commentMapper.selectList(lambdaQueryWrapper2);
                    }
                }

            if (!CollectionUtils.isEmpty(list1)){
                Set<Long> userIds1 = new HashSet<>();
                list1.forEach(record1->{
                    userIds1.add(record1.getFromUserId());
                    userIds1.add(record1.getToUserId());
                });

                Map<Long, User> map1 = userService.getUserMap(new ArrayList<>(userIds1));
                List<CommentVO> list2 = new ArrayList<>();
                list1.forEach(record1 ->{
                    Long fromUserId1 = record1.getFromUserId();
                    Long toUserId1 = record1.getToUserId();
                    CommentVO commentVO1 = new CommentVO();
                    BeanUtils.copyProperties(record1, commentVO1);
                    //From Info
                    if (map1.containsKey(fromUserId1)){
                        commentVO1.setFromNickName(map1.get(fromUserId1).getNickName());
                        commentVO1.setFromAvatarUrl(map1.get(fromUserId1).getAvatarUrl());
                        commentVO1.setFromUserId(fromUserId1);
                    }
                    //To Info
                    if (map1.containsKey(toUserId1)){
                        commentVO1.setToNickName(map1.get(toUserId1).getNickName());
                        commentVO1.setToAvatarUrl(map1.get(toUserId1).getAvatarUrl());
                        commentVO1.setToUserId(toUserId1);
                    }
                    //TODO
                    LambdaQueryWrapper<Like> lambdaQueryWrapper3 = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper3.eq(Like::getLikedType, LikedTypeEnum.COMMENT.getCode());
                    lambdaQueryWrapper3.eq(Like::getLikedId, commentVO1.getId());
                    commentVO1.setLikedNum((int) likeService.count(lambdaQueryWrapper3));

                    //登录状态下访问 判断有没有点赞
                    if (userId != 0L){
                        LambdaQueryWrapper<Like> lambdaQueryWrapper4 = new LambdaQueryWrapper<>();
                        lambdaQueryWrapper4.eq(Like::getUserId, userId);
                        lambdaQueryWrapper4.eq(Like::getStatus, LikeStatusEnum.NORMAL.getCode());
                        lambdaQueryWrapper4.eq(Like::getLikedId, commentVO1.getId());
                        commentVO1.setLiked(likeService.count(lambdaQueryWrapper4) > 0);
                    }
                    list2.add(commentVO1);
                });
                commentVO.setChildren(list2);
            }
            list.add(commentVO);
        });

        response.setList(list);

        return GlobalResponse.success(response);
    }

    @Override
    public GlobalResponse showAllCommentList(long toUserId, long opusId, int orderType) {
        List<CommentVO> list = new ArrayList<>();
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getToUserId, toUserId);
        lambdaQueryWrapper.eq(Comment::getOpusId, opusId);
        //TODO if (orderType == )
        List<Comment> commentList = commentMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(commentList)){
            return GlobalResponse.success("no data");
        }
        Set<Long> userIds = new HashSet<>();
        commentList.forEach(comment->{
            userIds.add(comment.getFromUserId());
            userIds.add(comment.getToUserId());
        });
        Map<Long, User> map = userService.getUserMap(new ArrayList<>(userIds));

        commentList.forEach(comment -> {
            Long fromUserId = comment.getFromUserId();
            CommentVO vo = new CommentVO();
            BeanUtils.copyProperties(comment, vo);

            vo.setGmtCreatedString(TimeUtil.getShortTime(vo.getGmtCreated()));
            //From Info
            if (map.containsKey(fromUserId)){
                vo.setFromNickName(map.get(fromUserId).getNickName());
                vo.setFromAvatarUrl(map.get(fromUserId).getAvatarUrl());
                vo.setFromUserId(fromUserId);
            }
            //To Info
            if (map.containsKey(toUserId)){
                vo.setToNickName(map.get(toUserId).getNickName());
                vo.setToAvatarUrl(map.get(toUserId).getAvatarUrl());
                vo.setToUserId(toUserId);
            }
            LambdaQueryWrapper<Like> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(Like::getLikedType, LikedTypeEnum.COMMENT.getCode());
            lambdaQueryWrapper1.eq(Like::getLikedId, vo.getId());
            vo.setLikedNum((int) likeService.count(lambdaQueryWrapper1));
            list.add(vo);
        });
        return GlobalResponse.success(list);
    }

    @Override
    public GlobalResponse addComment(AddCommentRequest addCommentRequest) {
        Comment comment = new Comment();
        BeanUtils.copyProperties(addCommentRequest, comment);
        Long opusId = addCommentRequest.getOpusId();
        Opus opus = opusService.getById(opusId);
        if (Objects.isNull(opus)){
            return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(), "invalid opus");
        }
        if (addCommentRequest.getToUserId() != 0){ //
            comment.setToUserId(addCommentRequest.getToUserId());
        }else {
            comment.setToUserId(opus.getUserId());
        }
        GlobalResponse checkContentResponse = contentService.checkContentSensitive(comment.getContent());
        comment.setContent(checkContentResponse.getCode() == GlobalResponseEnum.SUCCESS.getCode()? (String) checkContentResponse.getData() : comment.getContent());
        comment.setId(IDUtil.nextId());
        comment.setStatus(0);
        comment.setGmtCreated(LocalDateTime.now());
        comment.setGmtModified(LocalDateTime.now());
        int count = commentMapper.insert(comment);
        if (count > 0){
            return GlobalResponse.success(true);
        }
        return GlobalResponse.error(GlobalResponseEnum.ERROR.getCode(),"add error");
    }
}
