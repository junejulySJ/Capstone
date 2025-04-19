package com.capstone.meetingmap.user.controller;

import com.capstone.meetingmap.user.dto.*;
import com.capstone.meetingmap.user.service.UserService;
import com.capstone.meetingmap.util.SessionUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //아이디 중복 검사
    @PostMapping("/check-id")
    public ResponseEntity<?> checkUserId(@RequestBody UserCheckIdRequestDto userCheckIdRequestDto) {
        UserCheckIdResponseDto userCheckIdResponseDto = userService.existsByUserId(userCheckIdRequestDto);
        return ResponseEntity.ok(userCheckIdResponseDto);
    }

    //회원가입 처리
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        try {
            UserRegisterResponseDto userRegisterResponseDto = userService.join(userRegisterRequestDto);

            System.out.println("New User Registered!: "+ userRegisterRequestDto.getUserId());

            // 생성된 회원의 정보 URL을 Location 헤더에 설정
            URI location = URI.create("/users/" + userRegisterResponseDto.getUserId());

            return ResponseEntity.created(location).body(userRegisterResponseDto);
        } catch(IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // 중복 회원 존재 시 409 CONFLICT
        }
    }

    //회원 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
        try {
            System.out.println(SessionUtil.getLoggedInUserId());
            userService.checkCurrentUser(userId, SessionUtil.getLoggedInUserId()); // 요청한 회원 id와 세션의 회원 id 비교

            // 요청한 회원의 정보 조회
            Optional<UserResponseDto> optionalUserResponseDto = userService.findOne(userId);
            return optionalUserResponseDto
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build()); // 회원 정보가 없으면 404 NOT FOUND
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    //전체 회원 조회
    @GetMapping("/users")
    public ResponseEntity<?> list() {
        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("권한이 없습니다."); //세션이 없으면 접근 거부
        }
        try {
            userService.checkAdmin(SessionUtil.getLoggedInUserId()); //현재 사용자 정보로 관리자인지 검증

            List<UserResponseDto> users = userService.findAllUsers();
            return ResponseEntity.ok(users);
        } catch(SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch(EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
