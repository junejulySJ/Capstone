package com.capstone.meetingmap.category.service;

import com.capstone.meetingmap.category.dto.CategoryResponseDto;
import com.capstone.meetingmap.category.entity.Category;
import com.capstone.meetingmap.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto searchByCategoryNo(Integer categoryNo) {
        Category category = categoryRepository.findByCategoryNo(categoryNo)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return CategoryResponseDto.fromEntity(category); //카테고리 엔티티를 dto로 변환해서 반환
    }
}
