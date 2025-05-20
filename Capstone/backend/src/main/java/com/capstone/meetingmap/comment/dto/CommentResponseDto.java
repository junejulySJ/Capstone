package com.capstone.meetingmap.comment.dto;

import com.capstone.meetingmap.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private Integer commentNo;
    private String commentContent;
    private String userId;
    private String userNick;
    private Integer boardNo;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .commentNo(comment.getCommentNo())
                .commentContent(comment.getCommentContent())
                .userId(comment.getUser().getUserId())
                .userNick(comment.getUser().getUserNick())
                .boardNo(comment.getBoard().getBoardNo())
                .build();
    }
}
