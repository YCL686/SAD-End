package com.example.sharablead.response;

import lombok.Data;

import java.util.List;

/**
 * @Author YCL686
 * @Date 2022/10/14
 */
@Data
public class PageAccountEntryListResponse {

    private long total;

    private long pageSize;

    private long currentPageNo;

    private long totalPageNos;

    private List<AccountEntryVO> list;
}
