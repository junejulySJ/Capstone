package com.capstone.meetingmap.user.controller;

import com.capstone.meetingmap.user.dto.LoginRequestDto;
import com.capstone.meetingmap.user.dto.LoginResponseDto;
import com.capstone.meetingmap.user.dto.UserResponseDto;
import com.capstone.meetingmap.user.entity.User;
import com.capstone.meetingmap.user.service.UserService;
import com.capstone.meetingmap.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // 현재 로그인된 사용자 정보 확인 (프론트에서 세션 유지 확인용)
    @GetMapping("/session")
    public ResponseEntity<?> getSessionUser() {
        if (SessionUtil.getLoggedInUserId() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
        }
        UserResponseDto user = userService.findOne(SessionUtil.getLoggedInUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return ResponseEntity.ok(user); // 로그인된 사용자 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {

        try {
            LoginResponseDto loginResponseDto = userService.login(loginRequestDto);
            SessionUtil.setLoggedInUserId(loginResponseDto.getUserId()); // 로그인 성공 시 세션에 사용자 정보 저장
            System.out.println(loginResponseDto.getUserId() + " login success");

            return ResponseEntity.ok(loginResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // 로그아웃 처리 (세션 초기화)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SessionUtil.clearSession();
        return ResponseEntity.ok().body("Logged out");
    }
}
