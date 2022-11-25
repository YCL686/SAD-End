package com.example.sharablead.response;

import com.example.sharablead.entity.Opus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author YCL686
 * @Date 2022/10/16
 */
@Data
public class OpusVO {
    private Long id;

    private Long userId;

    private String title;

    private String summary;

//    private String text;
//
//    private String content;

    private Integer minted;

    private String mintedAddress;

    //private String resourceUrl;

    private Integer status;

    private LocalDateTime publishTime;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private String nickName;

    private String avatarUrl;

    private String characterSign;

    private List<String> resourceUrls;

    private List<String> resourceCompressUrls;

    private Integer likeNum;

    private Integer commentNum;

    private Integer collectNum;

    private Integer watchNum;

    private Boolean focused = false;

    private Boolean liked = false;

    private Boolean collected = false;

    private BigDecimal hotScore;

    private BigDecimal currentStakingAmount = BigDecimal.ZERO;

}
