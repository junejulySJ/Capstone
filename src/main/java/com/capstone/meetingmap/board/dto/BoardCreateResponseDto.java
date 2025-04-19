package com.capstone.meetingmap.board.dto;

import com.capstone.meetingmap.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateResponseDto {
    private String boardTitle;

    @Builder
    public BoardCreateResponseDto(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    //엔티티를 dto로 변환
    public static BoardCreateResponseDto fromEntity(Board board) {
        return BoardCreateResponseDto.builder()
                .boardTitle(board.getBoardTitle())
                .build();
    }
}
