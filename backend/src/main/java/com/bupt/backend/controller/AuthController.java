package com.bupt.backend.controller;

import com.bupt.backend.common.Result;
import com.bupt.backend.dto.AuthResponse;
import com.bupt.backend.dto.LoginRequest;
import com.bupt.backend.dto.RegisterRequest;
import com.bupt.backend.entity.User;
import com.bupt.backend.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("收到注册请求: {}", request.getUsername());
        try {
            Result<String> result = authService.register(request);
            logger.info("注册结果: {}", result);
            return result;
        } catch (Exception e) {
            logger.error("注册过程发生异常: ", e);
            return Result.error(500, "注册失败：" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @PostMapping("/update")
    public Result<User> updateUser(@Valid @RequestBody User user) {
        // 实际项目中应从安全上下文获取用户ID
        return authService.updateUser(user);
    }


    @PostMapping("/upload-avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file, Authentication authentication) {
        Long userId = ((User) authentication.getPrincipal()).getUserid();
        return authService.uploadAvatar(userId, file);
    }
    @GetMapping("/user/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        User user = authService.getUserById(id);
        return Result.success(user);
    }
}