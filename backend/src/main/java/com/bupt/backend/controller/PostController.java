package com.bupt.backend.controller;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Post;
import com.bupt.backend.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @PostMapping
    public Result<Post> createPost(@RequestBody Post post, @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
        return postService.createPost(post, coverImage);
    }

    @PutMapping("/{postId}")
    public Result<Post> updatePost(@PathVariable Integer postId, @RequestBody Post post, 
                                 @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
        post.setPostId(postId);
        return postService.updatePost(post, coverImage);
    }

    @PostMapping("/{postId}/publish")
    public Result<Post> publishPost(@PathVariable Integer postId) {
        return postService.publishPost(postId);
    }

    @GetMapping("/{postId}")
    public Result<Post> getPost(@PathVariable Integer postId) {
        return postService.getPostById(postId);
    }

    @GetMapping("/recent")
    public Result<List<Post>> getRecentPosts(Authentication authentication) {
        Integer userId = ((com.bupt.backend.entity.User) authentication.getPrincipal()).getUserid();
        return postService.getRecentPosts(userId, 2);
    }

    @GetMapping("/category/{categoryId}")
    public Result<List<Post>> getPostsByCategory(@PathVariable Integer categoryId) {
        return postService.getPostsByCategory(categoryId);
    }

    @PostMapping("/{postId}/cover")
    public Result<String> uploadCoverImage(@PathVariable Integer postId, @RequestParam("file") MultipartFile file) {
        return postService.uploadCoverImage(postId, file);
    }

    @PostMapping("/{postId}/view")
    public Result<Void> incrementViewCount(@PathVariable Integer postId) {
        return postService.incrementViewCount(postId);
    }
} 