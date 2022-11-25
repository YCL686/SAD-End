package com.example.sharablead.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetOpusByIdVO {

    private Long id;

    private Long userId;

    private String title;

    private String content;

    private Integer minted;

    private String mintedAddress;

    private Boolean self = false;

    private Integer status;

    private LocalDateTime publishTime;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private String nickName;

    private String avatarUrl;

    private String characterSign;

    private Integer likeNum = 0;

    private Integer commentNum = 0;

    private Integer collectNum = 0;

    private Integer watchNum = 0;

    private Boolean liked = false;

    private Boolean focused = false;

    private Boolean collected = false;

    private Boolean staked = false;

    private BigDecimal hotScore;

}
