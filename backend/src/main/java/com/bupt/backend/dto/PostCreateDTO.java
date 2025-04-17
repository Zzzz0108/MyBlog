package com.bupt.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostCreateDTO {
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题最长100个字符")
    private String title;

    @NotBlank
    private String content;

    private List<Integer> categoryIds;

    @Pattern(regexp = "draft|published", message = "状态不合法")
    private String status = "draft";
}
