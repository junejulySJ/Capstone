package com.capstone.meetingmap.board.dto;

import com.capstone.meetingmap.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardWriteResponseDto {
    private String boardTitle;

    //엔티티를 dto로 변환
    public static BoardWriteResponseDto fromEntity(Board board) {
        return BoardWriteResponseDto.builder()
                .boardTitle(board.getBoardTitle())
                .build();
    }
}
