package com.capstone.meetingmap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RouteController {
    //해당 경로는 스프링이 아닌 리액트에서 처리하도록 포워딩
    @RequestMapping(value = { "/", "/login/**", "/register/**", "/board/**", "/friend/**","/schedule/**", "/map/**" })
    public String forward() {
        return "forward:/index.html";
    }
}