package com.example.demo.controller.voteStore;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.voteStore.VoteStoreDto;
import com.example.demo.service.voteStore.VoteStoreService;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class VoteStoreController {

    private final VoteStoreService voteStoreService;
    private final GooglePlaceApi googlePlaceApi;
    private final HttpServletRequest request;

    public VoteStoreController(VoteStoreService voteStoreService, GooglePlaceApi googlePlaceApi, HttpServletRequest request) {
        this.voteStoreService = voteStoreService;
        this.googlePlaceApi = googlePlaceApi;
        this.request = request;
    }

    @RequestMapping("/voteStore")
    public String voteSotre() {
        return "voteStore";
    }

    @RequestMapping("/storeInfo/{date}")
    @ResponseBody
    public Map<String, Object> storeInfo(@PathVariable("date") String date) {

        HttpSession session = request.getSession(false);
        MemberDto sessionMember = (MemberDto) session.getAttribute("member");

        List<String> store = voteStoreService.getStoreId(date, sessionMember.getRoomCode());

        googlePlaceApi.init(store);
        List<JsonNode> storeList = googlePlaceApi.getData();
        List<VoteStoreDto> votelist = voteStoreService.getListOnDate(date, sessionMember.getRoomCode());

        Map<String, Object> result = new HashMap<>();

        for (VoteStoreDto voteStoreDto : votelist) {
            if (voteStoreDto.getId().equals(sessionMember.getId())) {
                result.put("myPlace", voteStoreDto.getStore_id());
            }
        }

        result.put("storeList", storeList);
        result.put("voteList", votelist);

        return result;
    }

    @ResponseBody
    @RequestMapping("/vote/{date}/{place_id}/{type}")
    public int vote(@PathVariable("date") String date, @PathVariable("place_id") String place_id, @PathVariable("type") String type) {

        int result = -1;

        LocalDateTime now = LocalDateTime.now();
        String comparisonDateTime = date + " 12:45";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime compare = LocalDateTime.parse(comparisonDateTime, formatter);

//        if (now.toLocalTime().isBefore(LocalTime.from(compare))) {

            HttpSession session = request.getSession(false);
            MemberDto login = (MemberDto) session.getAttribute("member");

            String end_yn = voteStoreService.getStatus(login.getId(), date);

            end_yn = end_yn == null ? "N" : end_yn;

            if (end_yn.equals("N")) {

                voteStoreService.deleteVote(place_id, login.getId(), date);
                result = 0; //삭제만 할경우는 결과를 0으로 반환
                
                if (type.equals("E")) { //D는 수정(투표 변경)
                    VoteStoreDto voteStoreDto = new VoteStoreDto();
                    voteStoreDto.setStore_id(place_id);
                    voteStoreDto.setId(login.getId());
                    voteStoreDto.setEnd_yn("N");

                    result = voteStoreService.insertVote(voteStoreDto);
                    System.out.println(result);
                }
            }
//        } else {
//            result = -2;
//        }

        return result;
    }

}
