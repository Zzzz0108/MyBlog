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
    private Integer userId;    // 文章作者
    private Integer categoryId; // 文章分类
    private String title;      // 文章标题
    private String content;    // 文章内容
    private String coverImage; // 封面图片
    private String status;     // 文章状态：draft/published
    private Integer viewCount; // 浏览次数
    private Integer likeCount; // 点赞次数

    @TableField("create_at")
    private LocalDateTime createAt;

    @TableField("update_at")
    private LocalDateTime updateAt;

    @TableField("publish_at")
    private LocalDateTime publishAt; // 发布时间
}
