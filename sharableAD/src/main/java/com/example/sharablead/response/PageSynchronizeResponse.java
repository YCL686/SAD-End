package com.example.sharablead.response;

import lombok.Data;

import java.util.List;
@Data
public class PageSynchronizeResponse {

    private long pageNo;

    private long pageSize;

    private long total;

    private long totalPages;

    private List<PageSynchronizeVO> data;
}
