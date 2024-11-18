package com.example.demo.controller.admin;

import com.example.demo.dto.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            MemberDto member = (MemberDto) session.getAttribute("member");
            if (member != null && "admin".equals(member.getId())) {
                return "admin";
            }
        }
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}