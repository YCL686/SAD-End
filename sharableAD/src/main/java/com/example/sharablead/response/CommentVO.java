package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentVO {

    private Long id;

    private Long opusId;

    private Long fromUserId;

    private Long toUserId;

    private Long parentId;

    private String fromNickName;

    private String toNickName;

    private String fromAvatarUrl;

    private String toAvatarUrl;

    private String content;

    private Integer likedNum;

    private LocalDateTime gmtCreated;

    private String gmtCreatedString;

    private Integer remainCommentNum = 0;

    private List<CommentVO> children;

    private Boolean liked = false;

    private Boolean showReply = false;

    private BigDecimal hotScore;

}
