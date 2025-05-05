package com.capstone.meetingmap.board.dto;

import com.capstone.meetingmap.board.entity.Board;
import com.capstone.meetingmap.category.entity.Category;
import com.capstone.meetingmap.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateRequestDto {
    @NotNull
    private String boardTitle;

    @NotNull
    private String boardContent;

    @NotNull
    private String userId;

    @NotNull
    private Integer categoryNo;

    @Builder
    public BoardCreateRequestDto(String boardTitle, String boardContent, String userId, Integer categoryNo) {
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.userId = userId;
        this.categoryNo = categoryNo;
    }

    //dto를 엔티티로 변환
    public Board toEntity(Category category, User user) {
        return Board.builder()
                .boardTitle(boardTitle)
                .boardContent(boardContent)
                .category(category)
                .user(user)
                .build();
    }
}
