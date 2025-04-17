package com.bupt.backend.controller;

import com.bupt.backend.common.Result;
import com.bupt.backend.dto.AuthResponse;
import com.bupt.backend.dto.LoginRequest;
import com.bupt.backend.dto.RegisterRequest;
import com.bupt.backend.entity.User;
import com.bupt.backend.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
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
        Integer userId = ((User) authentication.getPrincipal()).getUserid();
        return authService.uploadAvatar(userId, file);
    }
    @GetMapping("/user/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = authService.getUserById(id);
        return Result.success(user);
    }
}