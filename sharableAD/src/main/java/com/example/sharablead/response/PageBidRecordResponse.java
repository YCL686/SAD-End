package com.example.sharablead.response;

import lombok.Data;

import java.util.List;

@Data
public class PageBidRecordResponse {

    private Long pageNo;

    private Long pageSize;

    private Long total;

    private Long totalPages;

    private List<BidRecordVO> data;
}
