package com.capstone.algo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "🚀 알고있조 백엔드 서버 실행 중!";
    }
}
