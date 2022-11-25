package com.example.sharablead.response;

import com.example.sharablead.entity.Comment;
import com.example.sharablead.entity.Focus;
import com.example.sharablead.entity.Like;
import com.example.sharablead.entity.Opus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProfileVO {

    private Long userId;

    private boolean self;

    private String nickName;

    private String avatarUrl;

    private String characterSign;

    private String address;

    //被点赞数
    private Long likedNum;

    //被关注数即fans数
    private Long focusedNum;

    //TA关注的
    private Long focusNum;

    private Long opusNum;

    private Long commentNum;

    private LocalDateTime gmtCreated;

//    //TA的创作
//    private List<Opus> opusList;
//
//    //TA的评论
//    private List<Comment> commentList;
//
//    //TA的关注列表
//    private List<Focus> focusList;
//
//    //TA的收藏列表
//    private List<Opus> collectList;
//
//    //TA的点赞列表
//    private List<Like> likedList;
}
