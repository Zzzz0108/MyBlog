package com.bupt.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bupt.backend.entity.Post;
import com.bupt.backend.mapper.PostMapper;
import com.bupt.backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<PostDTO> getPostsByPage(Integer page, Integer size, String status) {
        Page<Post> postPage = new Page<>(page, size);
        IPage<Post> result = postMapper.selectPublishedPosts(postPage, status);

        return result.convert(post -> {
            PostDTO dto = new PostDTO();
            BeanUtils.copyProperties(post, dto);
            // 设置分类信息等
            return dto;
        });
    }

    // 其他方法实现...
}
