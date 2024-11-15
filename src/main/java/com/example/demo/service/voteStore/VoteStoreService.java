package com.example.demo.service.voteStore;

import com.example.demo.dao.voteStore.IVoteStoreDao;
import com.example.demo.dto.voteStore.VoteStoreDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteStoreService {

    @Autowired
    IVoteStoreDao voteStoreDao;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.api.key}")
    private String API_KEY;
    private static final String URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static List<String> place_keys;

    public VoteStoreService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public void init(List<String> places) {
        place_keys = places;
    }

    public List<JsonNode> getData() {

        List<JsonNode> data = new ArrayList<>();

        // place_keys값이 빈값이면 리턴 null
        if (place_keys == null) {
            return null;
        }

        for (String place_key : place_keys) {
            data.add(doApi(place_key));
        }

        return data;
    }

    private JsonNode doApi(String place_key) {

        JsonNode result = null;
        // API 요청
        try {

            String url = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("place_id", place_key)
                    .queryParam("key", API_KEY)
                    .build()
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            result = root.path("result");

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public List<VoteStoreDto> getList(){
        return voteStoreDao.getList();
    }

    public List<String> getStoreId(String date){
        return voteStoreDao.getStoreId(date);
    }

}
