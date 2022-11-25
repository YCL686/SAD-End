package com.example.sharablead.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author inncore
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 发送方 即from_user_id
     */
    private Long fromUserId;

    private Long toUserId;

    /**
     * 若parentId为0或null 表示一级评论
     */
    private Long parentId;

    /**
     * 若opusId为0或null 表示私信
     */
    private Long opusId;

    private String content;

    /**
     * 资源 考虑后续评论添加图片等媒体资源
     */
    private String resourceUrl;

    /**
     * 0未读 1已读
     */
    private Integer readed;
    /**
     * 0待审核 1已审核 2审核失败
     */
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreated;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;


}
