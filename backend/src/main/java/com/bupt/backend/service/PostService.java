package com.bupt.backend.service;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Result<Post> createPost(Post post, MultipartFile coverImage);
    Result<Post> updatePost(Post post, MultipartFile coverImage);
    Result<Post> publishPost(Long postId);
    Result<Post> getPostById(Long postId);
    Result<List<Post>> getRecentPosts(Long userId, Integer limit);
    Result<List<Post>> getPostsByCategory(Long categoryId);
    Result<String> uploadCoverImage(Long userId, MultipartFile file);
    Result<Void> incrementViewCount(Long postId);
} 