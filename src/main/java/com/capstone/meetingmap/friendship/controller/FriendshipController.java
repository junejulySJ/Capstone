package com.capstone.meetingmap.friendship.controller;

import com.capstone.meetingmap.friendship.dto.FriendshipSendRequestDto;
import com.capstone.meetingmap.friendship.service.FriendshipService;
import com.capstone.meetingmap.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    //친구 목록 조회
    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@PathVariable String userId) {
        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }
        return ResponseEntity.ok(friendshipService.getFriendships(SessionUtil.getLoggedInUserId()));
    }

    //보낸 요청 조회
    @GetMapping("/friends/sent")
    public ResponseEntity<?> getSentWaitingFriends(@PathVariable String userId) {
        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }
        return ResponseEntity.ok(friendshipService.getSentWaitingFriends(SessionUtil.getLoggedInUserId()));
    }

    //받은 요청 조회
    @GetMapping("/friends/received")
    public ResponseEntity<?> getReceivedWaitingFriends(@PathVariable String userId) {
        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }
        return ResponseEntity.ok(friendshipService.getReceivedWaitingFriends(SessionUtil.getLoggedInUserId()));
    }

    //친구 추가
    @PostMapping("/friends/add")
    public ResponseEntity<?> addFriend(@PathVariable String userId, @RequestBody FriendshipSendRequestDto friendshipSendRequestDto) {

        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }

        try {
            friendshipService.createFriendship(SessionUtil.getLoggedInUserId(), friendshipSendRequestDto);
            return ResponseEntity.ok(null);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 요청 수락
    @PostMapping("/friends/approve/{friendshipNo}")
    public ResponseEntity<?> approveFriendship(@PathVariable String userId, @PathVariable Integer friendshipNo) {
        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null); // 로그인 안 된 경우 접근 거부
        }
        friendshipService.approveFriendshipRequest(friendshipNo);
        return ResponseEntity.ok(null);
    }
}
