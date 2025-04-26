package com.capstone.meetingmap.auth.controller;

import com.capstone.meetingmap.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {
        String jwt = authService.kakaoLogin(code);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .build();
    }

    @PostMapping("/kakao/logout")
    public ResponseEntity<?> kakaoLogout(@RequestHeader("Authorization") String authorization) {
        System.out.println(authorization);
        authService.kakaoLogout(authorization);
        return ResponseEntity.ok().build();
    }
}
