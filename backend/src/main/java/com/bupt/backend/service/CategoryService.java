package com.bupt.backend.service;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Category;

import java.util.List;

public interface CategoryService {
    Result<Category> createCategory(Category category);
    Result<Category> updateCategory(Category category);
    Result<Void> deleteCategory(Long categoryId);
    Result<List<Category>> getUserCategories(Long userId);
    Result<Category> getCategoryById(Long categoryId);
    Result<Integer> getUserCategoryCount(Long userId);
    Result<List<Category>> getAllCategories();
} 