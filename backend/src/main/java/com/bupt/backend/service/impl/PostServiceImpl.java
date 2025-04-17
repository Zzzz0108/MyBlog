package com.bupt.backend.service.impl;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Post;
import com.bupt.backend.entity.User;
import com.bupt.backend.mapper.PostMapper;
import com.bupt.backend.mapper.UserMapper;
import com.bupt.backend.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Post> createPost(Post post, MultipartFile coverImage) {
        try {
            post.setCreateAt(LocalDateTime.now());
            post.setUpdateAt(LocalDateTime.now());
            post.setStatus("draft");
            post.setViewCount(0);
            post.setLikeCount(0);

            if (coverImage != null && !coverImage.isEmpty()) {
                String imageUrl = saveCoverImage(post.getUserId(), coverImage);
                post.setCoverImage(imageUrl);
            }

            postMapper.insert(post);
            return Result.success(post);
        } catch (Exception e) {
            logger.error("创建文章失败: ", e);
            return Result.error(500, "创建文章失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Post> updatePost(Post post, MultipartFile coverImage) {
        try {
            Post existingPost = postMapper.selectById(post.getPostId());
            if (existingPost == null) {
                return Result.error(404, "文章不存在");
            }

            post.setUpdateAt(LocalDateTime.now());
            if (coverImage != null && !coverImage.isEmpty()) {
                String imageUrl = saveCoverImage(post.getUserId(), coverImage);
                post.setCoverImage(imageUrl);
            }

            postMapper.updateById(post);
            return Result.success(post);
        } catch (Exception e) {
            logger.error("更新文章失败: ", e);
            return Result.error(500, "更新文章失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Post> publishPost(Integer postId) {
        try {
            Post post = postMapper.selectById(postId);
            if (post == null) {
                return Result.error(404, "文章不存在");
            }

            post.setStatus("published");
            post.setPublishAt(LocalDateTime.now());
            postMapper.updateById(post);

            // 更新用户文章数
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                user.setPostCount(user.getPostCount() + 1);
                userMapper.updateById(user);
            }

            return Result.success(post);
        } catch (Exception e) {
            logger.error("发布文章失败: ", e);
            return Result.error(500, "发布文章失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Post> getPostById(Integer postId) {
        Post post = postMapper.selectById(postId);
        if (post == null) {
            return Result.error(404, "文章不存在");
        }
        return Result.success(post);
    }

    @Override
    public Result<List<Post>> getRecentPosts(Integer userId, Integer limit) {
        List<Post> posts = postMapper.selectRecentPosts(userId, limit);
        return Result.success(posts);
    }

    @Override
    public Result<List<Post>> getPostsByCategory(Integer categoryId) {
        List<Post> posts = postMapper.selectByCategoryId(categoryId);
        return Result.success(posts);
    }

    @Override
    public Result<String> uploadCoverImage(Integer postId, MultipartFile file) {
        try {
            Post post = postMapper.selectById(postId);
            if (post == null) {
                return Result.error(404, "文章不存在");
            }

            String imageUrl = saveCoverImage(post.getUserId(), file);
            post.setCoverImage(imageUrl);
            postMapper.updateById(post);

            return Result.success(imageUrl);
        } catch (Exception e) {
            logger.error("上传封面图片失败: ", e);
            return Result.error(500, "上传封面图片失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> incrementViewCount(Integer postId) {
        try {
            postMapper.incrementViewCount(postId);
            return Result.success();
        } catch (Exception e) {
            logger.error("增加浏览次数失败: ", e);
            return Result.error(500, "增加浏览次数失败: " + e.getMessage());
        }
    }

    private String saveCoverImage(Integer userId, MultipartFile file) throws Exception {
        String uploadDir = "uploads/posts/" + userId + "/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;
        file.transferTo(new File(filePath));

        return "/" + filePath;
    }
} 