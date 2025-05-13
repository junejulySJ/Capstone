package com.capstone.meetingmap.map.repository;

import com.capstone.meetingmap.map.entity.PlaceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceCategoryRepository extends JpaRepository<PlaceCategory, Integer> {
    boolean existsByPlaceCategoryCode(String placeCategoryCode);
}
