package com.example.demo.controller.member;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * TODO: 추후 모든 뷰로 반환해주는 컨트롤러는 모아서 따로 처리
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("member") != null) {
            return "redirect:/";
        }
        return "/login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

}
