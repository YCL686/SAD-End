package com.example.sharablead.response;

import lombok.Data;

import java.util.List;

@Data
public class PageCommentListResponse {

    private Integer remainCommentNum = 0;

    private List<CommentVO> list;

}
