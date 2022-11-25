package com.example.sharablead.response;

import lombok.Data;

import java.util.List;

@Data
public class PageProfileOpusListResponse {

    private Boolean self = false;

    private Long total;

    private Long pageSize;

    private Long pageNo;

    private Long totalPages;

    private List<PageProfileOpusVO> list;
}
