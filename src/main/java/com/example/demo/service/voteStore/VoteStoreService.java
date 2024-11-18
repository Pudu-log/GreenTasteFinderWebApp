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

    private final IVoteStoreDao voteStoreDao;

    public VoteStoreService(IVoteStoreDao voteStoreDao) {
        this.voteStoreDao = voteStoreDao;
    }

    public List<VoteStoreDto> getList(){
        return voteStoreDao.getList();
    }

    public List<String> getStoreId(String date){
        return voteStoreDao.getStoreId(date);
    }

}
