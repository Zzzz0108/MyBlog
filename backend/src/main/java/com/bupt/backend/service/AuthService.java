package com.bupt.backend.service;

import com.bupt.backend.common.Result;
import com.bupt.backend.dto.AuthResponse;
import com.bupt.backend.dto.LoginRequest;
import com.bupt.backend.dto.RegisterRequest;
import com.bupt.backend.entity.User;
import org.springframework.web.multipart.MultipartFile;

// AuthService.java
public interface AuthService {
    Result<String> register(RegisterRequest request);
    Result<AuthResponse> login(LoginRequest request);
    Result<User> updateUser(User user);
    Result<String> uploadAvatar(Long userId, MultipartFile file);
    User getUserById(Long userId);
}

