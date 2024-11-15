package com.example.demo.controller.voteStore;

import com.example.demo.service.voteStore.VoteStoreService;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class VoteStoreController {

    @Autowired
    VoteStoreService voteStoreService;

    @Autowired
    GooglePlaceApi googlePlaceApi;

    @RequestMapping("/voteStore")
    public String voteSotre() {
        return "voteStore";
    }

    @RequestMapping("/storeInfo/{date}")
    @ResponseBody
    public List<JsonNode> storeInfo(@PathVariable("date") String date) {
        System.out.println(date);
        List<String> store = voteStoreService.getStoreId(date);

        googlePlaceApi.init(store);
        List<JsonNode> result = googlePlaceApi.getData();

        return result;
    }

}
