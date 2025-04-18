package com.bupt.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bupt.backend.common.Result;
import com.bupt.backend.dto.AuthResponse;
import com.bupt.backend.dto.LoginRequest;
import com.bupt.backend.dto.RegisterRequest;
import com.bupt.backend.entity.User;
import com.bupt.backend.mapper.UserMapper;
import com.bupt.backend.service.AuthService;
import com.bupt.backend.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;

// AuthServiceImpl.java
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Result<String> register(RegisterRequest request) {
        logger.info("开始注册用户: {}", request.getUsername());
        
        if (userMapper.existsByUsername(request.getUsername())) {
            logger.warn("用户名已存在: {}", request.getUsername());
            return Result.error(400, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreateAt(LocalDateTime.now());
        user.setPostCount(0);
        user.setLikeCount(0);
        user.setViewCount(0);
        user.setEmail(request.getUsername() + "@example.com"); // 设置默认邮箱
        user.setAvatar(""); // 设置默认头像为空
        user.setBio(""); // 设置默认简介为空

        try {
            logger.info("准备插入用户数据: {}", user);
            int result = userMapper.insert(user);
            logger.info("用户插入结果: {}", result);
            return Result.success("注册成功");
        } catch (Exception e) {
            logger.error("注册失败: ", e);
            return Result.error(500, "注册失败：" + e.getMessage());
        }
    }

    @Override
    public Result<AuthResponse> login(LoginRequest request) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, request.getUsername())
        );

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return Result.error(400, "用户名或密码错误");
        }

        // 获取 UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        
        // 生成 JWT token
        String token = jwtService.generateToken(userDetails);
        
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserid(user.getUserid());
        response.setUsername(user.getUsername());
        response.setAvatar(user.getAvatar());
        response.setBio(user.getBio());
        response.setPostCount(user.getPostCount());
        response.setLikeCount(user.getLikeCount());
        response.setViewCount(user.getViewCount());
        return Result.success(response);
    }

    @Override
    public Result<User> updateUser(User user) {
        User existingUser = userMapper.selectById(user.getUserid());
        if (existingUser == null) {
            return Result.error(404, "用户不存在");
        }

        // 只允许更新特定字段
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setBio(user.getBio());

        userMapper.updateById(existingUser);
        return Result.success(existingUser);
    }

    @Override
    public Result<String> uploadAvatar(Long userId, MultipartFile file) {
        try {
            User user = userMapper.selectById(userId);
            if (user == null) {
                return Result.error(404, "用户不存在");
            }

            // 简单实现：保存到本地 (生产环境应使用云存储)
            String uploadDir = "uploads/avatars/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String fileName = userId + "_" + System.currentTimeMillis() +
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String filePath = uploadDir + fileName;

            file.transferTo(new File(filePath));

            // 更新用户头像URL
            user.setAvatar("/" + filePath);
            userMapper.updateById(user);

            return Result.success(user.getAvatar());
        } catch (Exception e) {
            return Result.error(500, "头像上传失败: " + e.getMessage());
        }
    }

    @Override
    public User getUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    public UserMapper getUserMapper() {
        return userMapper;
    }

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
