package com.capstone.meetingmap.group.dto;

import com.capstone.meetingmap.group.entity.GroupBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class GroupBoardResponseDto {
    private Integer groupBoardNo;
    private String groupBoardTitle;
    private String groupBoardContent;
    private String userId;
    private String userNick;
    private Integer groupNo;
    private String groupTitle;
    private LocalDateTime groupBoardWriteDate;
    private LocalDateTime groupBoardUpdateDate;
    private List<GroupCommentResponseDto> comments;

    public static GroupBoardResponseDto fromEntity(GroupBoard groupBoard) {
        return GroupBoardResponseDto.builder()
                .groupBoardNo(groupBoard.getGroupBoardNo())
                .groupBoardTitle(groupBoard.getGroupBoardTitle())
                .groupBoardContent(groupBoard.getGroupBoardContent())
                .userId(groupBoard.getUser().getUserId())
                .userNick(groupBoard.getUser().getUserNick())
                .groupNo(groupBoard.getGroup().getGroupNo())
                .groupTitle(groupBoard.getGroup().getGroupTitle())
                .groupBoardWriteDate(groupBoard.getGroupBoardWriteDate())
                .groupBoardUpdateDate(groupBoard.getGroupBoardUpdateDate())
                .comments(groupBoard.getComments().stream().map(GroupCommentResponseDto::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
