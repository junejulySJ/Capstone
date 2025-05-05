package com.capstone.meetingmap.category.repository;

import com.capstone.meetingmap.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
