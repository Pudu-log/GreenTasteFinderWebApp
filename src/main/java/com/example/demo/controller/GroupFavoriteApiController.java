package com.example.demo.controller;

import com.example.demo.dao.IFavoriteGroupDao;
import com.example.demo.utils.GooglePlaceApi;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/group/")
public class GroupFavoriteApiController {
    private final GooglePlaceApi googlePlaceApi;
    private final IFavoriteGroupDao favoriteGroupDao;

    @Autowired
    public GroupFavoriteApiController(
            GooglePlaceApi googlePlaceApi, IFavoriteGroupDao favoriteGroupDao
    ) {
        this.googlePlaceApi = googlePlaceApi;
        this.favoriteGroupDao = favoriteGroupDao;
    }

    //gubn은 처음 들어갈때만 G로 하고 나머지는 gvf 마다 바꾸는걸로
    @RequestMapping("/favorites")
    public List<JsonNode> groupFavorite(
            HttpServletRequest request,
            @RequestParam String gubn,
            @RequestParam int limit,
            @RequestParam int offset
    ) {
        HttpSession session = request.getSession(false);

        List<String> favorite = favoriteGroupDao.getFavoriteGroup(
                "aaa100", gubn, limit, offset
//                (String)session.getAttribute("member"),"G"
        );
        googlePlaceApi.init(favorite);
        return googlePlaceApi.getData();
    }
}
