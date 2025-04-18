package com.bupt.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("SELECT * FROM categories WHERE user_id = #{userId} ORDER BY create_at DESC")
    List<Category> selectByUserId(Long userId);

    @Select("SELECT COUNT(*) FROM categories WHERE user_id = #{userId}")
    Integer countByUserId(Long userId);
} 