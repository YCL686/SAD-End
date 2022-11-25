package com.example.sharablead.service;

import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Comment;
import com.example.sharablead.request.AddCommentRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author inncore
 * @since 2022-10-18
 */
public interface CommentService extends IService<Comment> {

    GlobalResponse pageCommentList(long pageNo, long subPageNo, long opusId, int orderType, boolean showAllComments, boolean showAllSubComments, long subCommentParentId, Long userId);

    GlobalResponse showAllCommentList(long toUserId, long opusId, int orderType);

    GlobalResponse addComment(AddCommentRequest addCommentRequest);
}
