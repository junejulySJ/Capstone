package com.capstone.meetingmap.category.dto;

import com.capstone.meetingmap.category.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryResponseDto {
    private Integer categoryNo;
    private String categoryName;

    @Builder
    public CategoryResponseDto(Integer categoryNo, String categoryName) {
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
    }

    //엔티티를 dto로 변환
    public static CategoryResponseDto fromEntity(Category category) {
        return CategoryResponseDto.builder()
                .categoryNo(category.getCategoryNo())
                .categoryName(category.getCategoryName())
                .build();
    }
}
