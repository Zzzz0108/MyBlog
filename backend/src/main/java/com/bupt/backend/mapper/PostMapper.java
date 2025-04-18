package com.bupt.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.backend.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    @Select("SELECT * FROM posts WHERE user_id = #{userId} AND status = 'published' ORDER BY publish_at DESC LIMIT #{limit}")
    List<Post> selectRecentPosts(Long userId, Integer limit);

    @Select("SELECT * FROM posts WHERE category_id = #{categoryId} AND status = 'published' ORDER BY publish_at DESC")
    List<Post> selectByCategoryId(Long categoryId);

    @Select("SELECT COUNT(*) FROM posts WHERE user_id = #{userId} AND status = 'published'")
    Integer countPublishedPosts(Long userId);

    @Update("UPDATE posts SET view_count = view_count + 1 WHERE post_id = #{postId}")
    int incrementViewCount(Long postId);
}