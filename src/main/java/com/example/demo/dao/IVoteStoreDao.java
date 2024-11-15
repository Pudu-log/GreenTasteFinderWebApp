package com.example.demo.dao;

import com.example.demo.dto.VoteStoreDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IVoteStoreDao {

    public List<VoteStoreDto> getList();

}
