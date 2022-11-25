package com.example.sharablead.request;

import lombok.Data;

import java.io.Serializable;
@Data
public class PageRequest implements Serializable {

    private long pageNo = 1;

    private long pageSize = 5;
}
