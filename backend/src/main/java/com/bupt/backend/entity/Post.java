package com.bupt.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("posts")
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer postId;
    private Integer userId;

    private String title;
    private String slug;
    private String content;
    private String excerpt;
    private String coverImage;

    @TableField("status")
    private String status; // draft/published/archived

    private Integer viewCount;

    @TableField("create_at")
    private LocalDateTime createAt;

    @TableField("update_at")
    private LocalDateTime updateAt;

    private LocalDateTime publishedAt;
}
