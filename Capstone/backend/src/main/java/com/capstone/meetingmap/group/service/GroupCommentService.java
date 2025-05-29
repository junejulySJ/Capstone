package com.capstone.meetingmap.group.service;

import com.capstone.meetingmap.group.dto.GroupCommentRequestDto;
import com.capstone.meetingmap.group.dto.GroupCommentResponseDto;
import com.capstone.meetingmap.group.entity.GroupBoard;
import com.capstone.meetingmap.group.entity.GroupComment;
import com.capstone.meetingmap.group.repository.GroupBoardRepository;
import com.capstone.meetingmap.group.repository.GroupCommentRepository;
import com.capstone.meetingmap.group.repository.GroupMemberRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class GroupCommentService {

    private final GroupCommentRepository groupCommentRepository;
    private final UserRepository userRepository;
    private final GroupBoardRepository groupBoardRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupCommentService(GroupCommentRepository groupCommentRepository, UserRepository userRepository, GroupBoardRepository groupBoardRepository, GroupMemberRepository groupMemberRepository) {
        this.groupCommentRepository = groupCommentRepository;
        this.userRepository = userRepository;
        this.groupBoardRepository = groupBoardRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    // 댓글 작성
    @Transactional
    public GroupCommentResponseDto write(Integer groupNo, Integer groupBoardNo, GroupCommentRequestDto groupCommentRequestDto, String userId) {
        if (!groupMemberRepository.existsByGroup_GroupNoAndUser_UserId(groupNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 속한 그룹의 글에만 댓글을 작성할 수 있습니다");

        // 실제 존재하는 회원 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        // 실제 존재하는 게시글 가져오기
        GroupBoard groupBoard = groupBoardRepository.findById(groupBoardNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다"));

        GroupComment groupComment = groupCommentRequestDto.toEntity(user, groupBoard);

        groupCommentRepository.save(groupComment);

        return GroupCommentResponseDto.fromEntity(groupComment);
    }
}
