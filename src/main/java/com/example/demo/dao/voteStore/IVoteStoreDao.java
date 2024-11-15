package com.example.demo.dao.voteStore;

import com.example.demo.dto.voteStore.VoteStoreDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IVoteStoreDao {

    public List<VoteStoreDto> getList();

    public List<String> getStoreId(String date);

}
