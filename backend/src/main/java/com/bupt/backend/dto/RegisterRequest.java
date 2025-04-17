package com.bupt.backend.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// RegisterRequest.java
@Data
public class RegisterRequest {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名长度必须在4-16个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    public @NotBlank(message = "用户名不能为空") @Size(min = 4, max = 16, message = "用户名长度必须在4-16个字符之间") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "用户名不能为空") @Size(min = 4, max = 16, message = "用户名长度必须在4-16个字符之间") String username) {
        this.username = username;
    }

    public @NotBlank(message = "密码不能为空") @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "密码不能为空") @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间") String password) {
        this.password = password;
    }
}
