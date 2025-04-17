package com.bupt.backend.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.backend.entity.User;
import org.apache.ibatis.annotations.Select;

// 在UserMapper.java中添加自定义方法
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT EXISTS(SELECT 1 FROM users WHERE username = #{username})")
    boolean existsByUsername(String username);
    
    User selectByUsername(String username);
}