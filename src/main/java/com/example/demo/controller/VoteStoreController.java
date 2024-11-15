package com.example.demo.controller;

import com.example.demo.dto.VoteStoreDto;
import com.example.demo.service.VoteStoreService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class VoteStoreController {

    @Autowired
    VoteStoreService voteStoreService;

    @RequestMapping("/voteStore")
    public String voteSotre() {



        return "voteStore";
    }

    @RequestMapping("/voteStore/get")
    public List<JsonNode> getData() {

        List<VoteStoreDto> store = voteStoreService.getList();

//        voteStoreService.init(store);
        List<JsonNode> result = voteStoreService.getData();

        for (JsonNode node : result) {
            System.out.println(node);
        }

        return result;
    }

}
