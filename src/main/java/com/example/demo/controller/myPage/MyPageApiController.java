package com.example.demo.controller.myPage;

import com.example.demo.service.MyPageService;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/getFavorList")
    public List<JsonNode> getFavorList(@RequestParam("val") String val, @RequestParam("id") String id) {
        List<String> myPage = myPageService.getActStoreList(val, id);
        googlePlaceApi.init(myPage);
//        System.out.println(googlePlaceApi.getData());
        return googlePlaceApi.getData();
    }

    @GetMapping("/getActStoreList")
    public List<JsonNode> getActStoreList(@RequestParam("gubn") String gubn, @RequestParam("id") String id) {
        List<String> myPage = myPageService.getActStoreList(gubn, id);
        googlePlaceApi.init(myPage);
//        System.out.println(googlePlaceApi.getData());
        return googlePlaceApi.getData();
    }
}

