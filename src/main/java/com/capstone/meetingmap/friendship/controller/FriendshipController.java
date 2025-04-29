package com.capstone.meetingmap.friendship.controller;

import com.capstone.meetingmap.friendship.dto.FriendshipSendRequestDto;
import com.capstone.meetingmap.friendship.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    //친구 목록 조회
    @GetMapping
    public ResponseEntity<?> getFriends() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(friendshipService.getFriendships(userId));
    }

    //보낸 친구 요청 조회
    @GetMapping("/sent")
    public ResponseEntity<?> getSentWaitingFriends() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(friendshipService.getSentWaitingFriends(userId));
    }

    //받은 친구 요청 조회
    @GetMapping("/received")
    public ResponseEntity<?> getReceivedWaitingFriends() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(friendshipService.getReceivedWaitingFriends(userId));
    }

    //친구 추가
    @PostMapping("/add")
    public ResponseEntity<?> addFriend(@RequestBody FriendshipSendRequestDto friendshipSendRequestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        friendshipService.createFriendship(userId, friendshipSendRequestDto);
        return ResponseEntity.ok(null);
    }

    //친구 요청 수락
    @PostMapping("/approve/{friendshipNo}")
    public ResponseEntity<?> approveFriendship(@PathVariable Integer friendshipNo) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        friendshipService.approveFriendshipRequest(userId, friendshipNo);
        return ResponseEntity.ok(null);
    }
}
