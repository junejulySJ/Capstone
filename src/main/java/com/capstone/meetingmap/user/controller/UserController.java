package com.capstone.meetingmap.user.controller;

import com.capstone.meetingmap.user.dto.UserCheckIdRequestDto;
import com.capstone.meetingmap.user.dto.UserCheckIdResponseDto;
import com.capstone.meetingmap.user.dto.UserRegisterRequestDto;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import com.capstone.meetingmap.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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
        UserResponseDto userResponseDto = userService.join(userRegisterRequestDto);

        log.info("New User Registered!: {}", userRegisterRequestDto.getUserId());

        // 생성된 회원의 정보 URL을 Location 헤더에 설정
        URI location = URI.create("/user/" + userResponseDto.getUserId());

        return ResponseEntity.created(location).body(userResponseDto);
    }

    //회원 본인 조회
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") String userId) {
        UserResponseDto userResponseDto = userService.findOne(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    //전체 회원 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> list() {
        List<UserResponseDto> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }
}
