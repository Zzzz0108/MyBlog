package com.bupt.backend.dto;


import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Integer userid;
    private String username;
    private String avatar;
    private String email;
    private String bio;
    private Integer postCount;
    private Integer likeCount;
    private Integer viewCount;
}
