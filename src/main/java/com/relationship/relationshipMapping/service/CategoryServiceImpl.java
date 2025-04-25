package com.relationship.relationshipMapping.service;

import com.relationship.relationshipMapping.dto.Mapper;
import com.relationship.relationshipMapping.dto.requestDto.CategoryRequestDto;
import com.relationship.relationshipMapping.dto.responseDto.CategoryResponseDto;
import com.relationship.relationshipMapping.model.Category;
import com.relationship.relationshipMapping.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()-> new IllegalArgumentException("Could not find category with id:" + categoryId));
    }

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
       Category category = new Category();
       category.setName(categoryRequestDto.getName());
       categoryRepository.save(category);
       return Mapper.categoryToCategoryResponseDto(category);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        Category category = getCategory(categoryId);
        return Mapper.categoryToCategoryResponseDto(category);
    }

    @Override
    public List<CategoryResponseDto> getCategories() {
       List<Category> categories = StreamSupport.stream(categoryRepository.findAll().spliterator(), false)
               .collect(Collectors.toList());
       return Mapper.categoriesToCategoryResponseDtos(categories);
    }

    @Override
    public CategoryResponseDto deleteCategory(Long categoryId) {
    Category  category = getCategory(categoryId);
    categoryRepository.delete(category);
    return Mapper.categoryToCategoryResponseDto(category);
    }
    @Transactional
    @Override
    public CategoryResponseDto editCategory(Long categoryId, CategoryRequestDto categoryRequestDto) {
       Category categoryToEdit = getCategory(categoryId);
       categoryToEdit.setName(categoryRequestDto.getName());
       return Mapper.categoryToCategoryResponseDto(categoryToEdit);
    }
}
