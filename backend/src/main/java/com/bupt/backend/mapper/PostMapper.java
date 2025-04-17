package com.bupt.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.backend.entity.Post;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PostMapper extends BaseMapper<Post> {
    // 自定义复杂查询
    @Select("SELECT p.* FROM posts p " +
            "JOIN post_categories pc ON p.post_id = pc.post_id " +
            "WHERE pc.category_id = #{categoryId}")
    List<Post> selectByCategoryId(Integer categoryId);

    // 分页查询
    IPage<Post> selectPublishedPosts(Page<Post> page, @Param("status") String status);
}