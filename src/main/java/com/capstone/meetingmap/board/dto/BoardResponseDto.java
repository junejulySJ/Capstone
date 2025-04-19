package com.capstone.meetingmap.board.dto;

import com.capstone.meetingmap.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDto {
    private Integer boardNo;
    private String boardTitle;
    private String boardContent;
    private Integer boardViewCount;
    private LocalDateTime boardWriteDate;
    private LocalDateTime boardUpdateDate;
    private String userNick;
    private Integer categoryNo;
    private String categoryName;

    @Builder
    public BoardResponseDto(Integer boardNo, String boardTitle, String boardContent, Integer boardViewCount,
                            LocalDateTime boardWriteDate, LocalDateTime boardUpdateDate, String userNick,
                            Integer categoryNo, String categoryName) {
        this.boardNo = boardNo;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardViewCount = boardViewCount;
        this.boardWriteDate = boardWriteDate;
        this.boardUpdateDate = boardUpdateDate;
        this.userNick = userNick;
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
    }

    //엔티티를 dto로 변환
    public static BoardResponseDto fromEntity(Board board) {
        return BoardResponseDto.builder()
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
