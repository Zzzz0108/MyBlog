package com.bupt.backend.service;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    Result<Post> createPost(Post post, MultipartFile coverImage);
    Result<Post> updatePost(Post post, MultipartFile coverImage);
    Result<Post> publishPost(Integer postId);
    Result<Post> getPostById(Integer postId);
    Result<List<Post>> getRecentPosts(Integer userId, Integer limit);
    Result<List<Post>> getPostsByCategory(Integer categoryId);
    Result<String> uploadCoverImage(Integer userId, MultipartFile file);
    Result<Void> incrementViewCount(Integer postId);
} 