package com.bupt.backend.controller;

import com.bupt.backend.common.Result;
import com.bupt.backend.entity.Category;
import com.bupt.backend.entity.User;
import com.bupt.backend.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result<Category> createCategory(@RequestBody Category category, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        category.setUserId(user.getUserid());
        return categoryService.createCategory(category);
    }

    @PutMapping("/{categoryId}")
    public Result<Category> updateCategory(@PathVariable Integer categoryId, @RequestBody Category category) {
        category.setCategoryId(categoryId);
        return categoryService.updateCategory(category);
    }

    @DeleteMapping("/{categoryId}")
    public Result<Void> deleteCategory(@PathVariable Integer categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    @GetMapping
    public Result<List<Category>> getUserCategories(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return categoryService.getUserCategories(user.getUserid());
    }

    @GetMapping("/{categoryId}")
    public Result<Category> getCategory(@PathVariable Integer categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping("/count")
    public Result<Integer> getUserCategoryCount(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return categoryService.getUserCategoryCount(user.getUserid());
    }

    @GetMapping("/all")
    public Result<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }
} 