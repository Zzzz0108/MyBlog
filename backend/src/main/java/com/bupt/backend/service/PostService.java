package com.bupt.backend.service;

import com.bupt.backend.dto.PostCreateDTO;

public interface PostService {
    Page<PostDTO> getPostsByPage(Integer page, Integer size, String status);
    PostDetailDTO getPostDetail(Integer postId);
    Integer createPost(PostCreateDTO dto);
    void updatePost(PostUpdateDTO dto);
    void deletePost(Integer postId);
}
