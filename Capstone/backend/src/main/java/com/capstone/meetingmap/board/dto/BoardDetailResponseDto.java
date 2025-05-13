package com.capstone.meetingmap.board.dto;

import com.capstone.meetingmap.board.entity.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardDetailResponseDto {
    private Integer boardNo;
    private String boardTitle;
    private String boardContent;
    private Integer boardViewCount;
    private LocalDateTime boardWriteDate;
    private LocalDateTime boardUpdateDate;
    private String userNick;
    private Integer categoryNo;
    private String categoryName;

    //엔티티를 dto로 변환
    public static BoardDetailResponseDto fromEntity(Board board) {
        return BoardDetailResponseDto.builder()
                .boardNo(board.getBoardNo())
                .boardTitle(board.getBoardTitle())
                .boardContent(board.getBoardContent())
                .boardViewCount(board.getBoardViewCount())
                .boardWriteDate(board.getBoardWriteDate())
                .boardUpdateDate(board.getBoardUpdateDate())
                .userNick(board.getUser().getUserNick())
                .categoryNo(board.getCategory().getCategoryNo())
                .categoryName(board.getCategory().getCategoryName())
                .build();
    }
}
