package com.capstone.meetingmap.category.repository;

import com.capstone.meetingmap.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findByCategoryNo(Integer categoryNo);
    List<Category> findAll();
}
