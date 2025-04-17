package com.bupt.backend.service;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Category;

import java.util.List;

public interface CategoryService {
    Result<Category> createCategory(Category category);
    Result<Category> updateCategory(Category category);
    Result<Void> deleteCategory(Integer categoryId);
    Result<List<Category>> getUserCategories(Integer userId);
    Result<Category> getCategoryById(Integer categoryId);
    Result<Integer> getUserCategoryCount(Integer userId);
} 