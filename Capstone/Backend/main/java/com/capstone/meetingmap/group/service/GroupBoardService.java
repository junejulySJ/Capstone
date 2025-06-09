package com.capstone.meetingmap.group.service;

import com.capstone.meetingmap.group.dto.GroupBoardRequestDto;
import com.capstone.meetingmap.group.dto.GroupBoardResponseDto;
import com.capstone.meetingmap.group.entity.Group;
import com.capstone.meetingmap.group.entity.GroupBoard;
import com.capstone.meetingmap.group.repository.GroupBoardRepository;
import com.capstone.meetingmap.group.repository.GroupMemberRepository;
import com.capstone.meetingmap.group.repository.GroupRepository;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupBoardService {

    private final GroupBoardRepository groupBoardRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

    public GroupBoardService(GroupBoardRepository groupBoardRepository, UserRepository userRepository, GroupMemberRepository groupMemberRepository, GroupRepository groupRepository) {
        this.groupBoardRepository = groupBoardRepository;
        this.userRepository = userRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupRepository = groupRepository;
    }

    // 게시글(댓글 포함) 보기
    public List<GroupBoardResponseDto> searchArticles(Integer groupNo, String userId) {

        if (!groupMemberRepository.existsByGroup_GroupNoAndUser_UserId(groupNo, userId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신이 속한 그룹의 글만 조회할 수 있습니다");

        List<GroupBoard> groupBoardList = groupBoardRepository.findAllByGroup_GroupNoOrderByGroupBoardUpdateDateDesc(groupNo);
        return groupBoardList.stream().map(GroupBoardResponseDto::fromEntity).collect(Collectors.toList());
    }

    // 게시글 생성
    @Transactional
    public Integer write(Integer groupNo, GroupBoardRequestDto groupBoardRequestDto, String userId) {

        // 실제 존재하는 회원 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        // 실제 존재하는 그룹 가져오기
        Group group = groupRepository.findById(groupNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "그룹 정보를 찾을 수 없습니다"));

        GroupBoard groupBoard = groupBoardRepository.save(groupBoardRequestDto.toEntity(user, group));

        return groupBoard.getGroupBoardNo();
    }

    // 게시글 삭제
    @Transactional
    public void delete(Integer groupNo, Integer groupBoardNo, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다"));

        Group group = groupRepository.findById(groupNo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "그룹 정보를 찾을 수 없습니다"));

        if (user.getUserRole().getUserType() != 0 || !group.getGroupCreatedUser().getUserId().equals(userId)) { // 관리자나 그룹 생성자가 아니면
            // 작성자만 삭제 가능
            if (!groupBoardRepository.existsByGroupBoardNoAndUser_UserId(groupBoardNo, userId))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없거나 삭제 권한이 없습니다");
        }

        groupBoardRepository.deleteById(groupBoardNo);
    }
}
