package com.example.sharablead.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.sharablead.common.GlobalResponse;
import com.example.sharablead.entity.Token;
import com.example.sharablead.enums.GlobalResponseEnum;
import com.example.sharablead.request.AddCommentRequest;
import com.example.sharablead.service.CommentService;
import com.example.sharablead.util.TokenUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author inncore
 * @since 2022-10-18
 */
@Slf4j
@RestController
@RequestMapping("/comment")
public class CommentController {

    private static final String APISTR = "comment operation";

    @Autowired
    private CommentService commentService;

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "pageCommentList", notes = APISTR + "pageCommentList")
    @GetMapping("/pageCommentList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse pageCommentList(@RequestParam(value = "opusId", required = true, defaultValue = "0") long opusId,
                                          @RequestParam(value = "pageNo", required=true, defaultValue = "1") long pageNo,
                                          @RequestParam(value = "subPageNo", required = true, defaultValue = "1") long subPageNo,
                                          @RequestParam(value = "orderType", required=true, defaultValue = "0") int orderType,
                                          @RequestParam(value = "showAllComments", required = false, defaultValue = "false") boolean showAllComments,
                                          @RequestParam(value = "showAllSubComments", required = false, defaultValue = "false") boolean showAllSubComments,
                                          @RequestParam(value = "subCommentParentId", required = false, defaultValue = "0") long subCommentParentId,
                                          HttpServletRequest httpServletRequest
    ) {
        String token = httpServletRequest.getHeader("token");
        Long userId = 0L;
        if (StringUtils.isNotBlank(token)){
            try {
                Token token1 = tokenUtil.parseToken(token);
                userId = token1.getUserId();
            }catch (Exception e){
                log.error("pageCommentList abnormal: token is carried but invalid");
            }
        }        return commentService.pageCommentList(pageNo,subPageNo, opusId, orderType, showAllComments, showAllSubComments, subCommentParentId,userId);
    }

    /**
     * 点击展示更多评论调用 opusId必传 commentId不传表示一级评论的全部 传了表示二级评论的全部
     * @param toUserId
     * @param opusId
     * @param orderType
     * @return
     */
    @ApiOperation(value = "showAllCommentList", notes = APISTR + "showAllCommentList")
    @GetMapping("/showAllCommentList")
    @ApiOperationSupport(order = 1)
    public GlobalResponse showAllCommentList(@RequestParam(value = "toUserId", required = true, defaultValue = "0") long toUserId, @RequestParam(value = "opusId", required = true, defaultValue = "0") long opusId, @RequestParam(value = "orderType", required=true, defaultValue = "0") int orderType) {
        return commentService.showAllCommentList(toUserId, opusId, orderType);
    }

    /**
     * add a comment
     * @param addCommentRequest
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "addComment", notes = APISTR + "addComment")
    @PostMapping("/addComment")
    @ApiOperationSupport(order = 1)
    public GlobalResponse showAllCommentList(@RequestBody AddCommentRequest addCommentRequest, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Token token1 = tokenUtil.parseToken(token);
        addCommentRequest.setFromUserId(token1.getUserId());
        return commentService.addComment(addCommentRequest);
    }


}
