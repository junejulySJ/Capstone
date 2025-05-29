package com.capstone.meetingmap.group.controller;

import com.capstone.meetingmap.group.dto.GroupBoardRequestDto;
import com.capstone.meetingmap.group.service.GroupBoardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/groups/{groupNo}/boards")
public class GroupBoardController {
    private final GroupBoardService groupBoardService;

    public GroupBoardController(GroupBoardService groupBoardService) {
        this.groupBoardService = groupBoardService;
    }

    // 그룹 전체 게시글 조회
    @GetMapping
    public ResponseEntity<?> search(@PathVariable("groupNo") Integer groupNo) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(groupBoardService.searchArticles(groupNo, userId));
    }

    // 그룹 게시글 등록
    @PostMapping
    public ResponseEntity<?> write(@PathVariable Integer groupNo, @Valid @RequestBody GroupBoardRequestDto groupBoardRequestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        Integer groupBoardNo = groupBoardService.write(groupNo, groupBoardRequestDto, userId);

        URI location = URI.create("/api/groups/" + groupNo + "/boards/" + groupBoardNo);

        return ResponseEntity.created(location).build();
    }

    // 그룹 게시글 삭제
    @DeleteMapping("/{groupBoardNo}")
    public ResponseEntity<?> delete(@PathVariable Integer groupNo, @PathVariable Integer groupBoardNo) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        groupBoardService.delete(groupNo, groupBoardNo, userId);

        return ResponseEntity.noContent().build();
    }
}
