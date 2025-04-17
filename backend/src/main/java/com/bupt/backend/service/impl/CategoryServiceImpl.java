package com.bupt.backend.service.impl;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Category;
import com.bupt.backend.mapper.CategoryMapper;
import com.bupt.backend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result<Category> createCategory(Category category) {
        try {
            category.setCreateAt(LocalDateTime.now());
            category.setUpdateAt(LocalDateTime.now());
            category.setPostCount(0);
            categoryMapper.insert(category);
            return Result.success(category);
        } catch (Exception e) {
            logger.error("创建分类失败: ", e);
            return Result.error(500, "创建分类失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Category> updateCategory(Category category) {
        try {
            Category existingCategory = categoryMapper.selectById(category.getCategoryId());
            if (existingCategory == null) {
                return Result.error(404, "分类不存在");
            }

            category.setUpdateAt(LocalDateTime.now());
            categoryMapper.updateById(category);
            return Result.success(category);
        } catch (Exception e) {
            logger.error("更新分类失败: ", e);
            return Result.error(500, "更新分类失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteCategory(Integer categoryId) {
        try {
            Category category = categoryMapper.selectById(categoryId);
            if (category == null) {
                return Result.error(404, "分类不存在");
            }

            categoryMapper.deleteById(categoryId);
            return Result.success();
        } catch (Exception e) {
            logger.error("删除分类失败: ", e);
            return Result.error(500, "删除分类失败: " + e.getMessage());
        }
    }

    @Override
    public Result<List<Category>> getUserCategories(Integer userId) {
        try {
            List<Category> categories = categoryMapper.selectByUserId(userId);
            return Result.success(categories);
        } catch (Exception e) {
            logger.error("获取用户分类失败: ", e);
            return Result.error(500, "获取用户分类失败: " + e.getMessage());
        }
    }

    @Override
    public Result<Category> getCategoryById(Integer categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return Result.error(404, "分类不存在");
        }
        return Result.success(category);
    }

    @Override
    public Result<Integer> getUserCategoryCount(Integer userId) {
        try {
            Integer count = categoryMapper.countByUserId(userId);
            return Result.success(count);
        } catch (Exception e) {
            logger.error("获取用户分类数量失败: ", e);
            return Result.error(500, "获取用户分类数量失败: " + e.getMessage());
        }
    }
} 