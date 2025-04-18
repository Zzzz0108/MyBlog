package com.bupt.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("categories")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long categoryId;
    
    @TableField("user_id")
    private Long userId;  // 分类所属用户
    
    private String name;     // 分类名称
    private String description; // 分类描述
    
    @TableField("post_count")
    private Integer postCount; // 该分类下的文章数

    @TableField("create_at")
    private LocalDateTime createAt;

    @TableField("update_at")
    private LocalDateTime updateAt;
}
