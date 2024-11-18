package com.example.demo.controller.member;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.RoomDto;
import com.example.demo.service.member.MemberService;
import com.example.demo.type.ResponseStatus;
import com.example.demo.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;

    @Autowired
    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<String>> join(@RequestBody MemberDto memberDto) {
        System.out.println(memberDto);
        int result = memberService.memberInsert(memberDto);
        if (result > 0) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "회원가입 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.BAD_REQUEST, "회원가입 실패"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody MemberDto memberDto, HttpServletRequest request) {
        MemberDto result = memberService.login(memberDto.getId(), memberDto.getPw());
        if (result != null && !result.getId().trim().isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("member", result);
            session.setMaxInactiveInterval(60 * 60);

            // ID가 "admin"인 경우 어드민 페이지로 이동
            if ("admin".equals(result.getId())) {
                return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "/admin"));
            }
            // 일반 사용자라면 메인 페이지로 이동
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "/"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse<>(ResponseStatus.BAD_REQUEST, "로그인 실패"));
        }
    }

    @GetMapping("/login/select-box")
    public ResponseEntity<ApiResponse<List<RoomDto>>> selectBox() {
        List<RoomDto> selectBoxList = memberService.getSelectBox();
        return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, selectBoxList));
    }

    @GetMapping("/login/id-check")
    public ResponseEntity<ApiResponse<String>> idCheck(@RequestParam("id") String id) {
        int result = memberService.idCheck(id);
        if (result > 0) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "아이디 중복"));
        } else {
            return ResponseEntity.ok(new ApiResponse<>(ResponseStatus.SUCCESS, "아이디 사용가능"));
        }
    }
}