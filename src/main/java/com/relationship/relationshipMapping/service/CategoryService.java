package com.relationship.relationshipMapping.service;

import com.relationship.relationshipMapping.dto.requestDto.CategoryRequestDto;
import com.relationship.relationshipMapping.dto.responseDto.CategoryResponseDto;
import com.relationship.relationshipMapping.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    public Category getCategory(Long categoryId);
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto);
    public CategoryResponseDto getCategoryById(Long categoryId);
    public List<CategoryResponseDto> getCategories();
    public CategoryResponseDto deleteCategory(Long categoryId);
    public CategoryResponseDto editCategory(Long categoryId, CategoryRequestDto categoryRequestDto);
}
