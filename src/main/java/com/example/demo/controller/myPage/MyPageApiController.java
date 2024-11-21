package com.example.demo.controller.myPage;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.MyPageService;
import com.example.demo.type.ResponseStatus;
import com.example.demo.utils.ApiResponse;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/my")
public class MyPageApiController {
    private final MyPageService myPageService;
    private final GooglePlaceApi googlePlaceApi;

    @Autowired
    public MyPageApiController(MyPageService myPageService, GooglePlaceApi googlePlaceApi) {
        this.myPageService = myPageService;
        this.googlePlaceApi = googlePlaceApi;
    }
    @PostMapping("/memberUpdate")
    public ResponseEntity<ApiResponse<String>> memberUpdate(@RequestBody MemberDto memberDto) {
        System.out.println(memberDto);
        int result = myPageService.memberUpdate(memberDto);
        if (result > 0) {
            return ResponseEntity.ok(new ApiResponse<>(com.example.demo.type.ResponseStatus.SUCCESS, "수정 성공"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(ResponseStatus.BAD_REQUEST, "수정 실패"));
        }
    }

    @GetMapping("/getActStoreList")
    public List<JsonNode> getActStoreList(@RequestParam("gubn") String gubn, @RequestParam("id") String id) {
        List<String> myPage = myPageService.getActStoreList(gubn, id);
        googlePlaceApi.init(myPage);

        return googlePlaceApi.getData();
    }
}

