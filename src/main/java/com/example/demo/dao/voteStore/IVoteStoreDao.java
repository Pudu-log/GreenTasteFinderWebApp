package com.example.demo.dao.voteStore;

import com.example.demo.dto.voteStore.VoteStoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IVoteStoreDao {

    public List<VoteStoreDto> getList();

    public List<VoteStoreDto> getListOnDate(String date, int room_code);

    public List<String> getStoreId(String date, int room_code);

    public void deleteVote(String place_id, String id, String date);

    public int insertVote(@Param("vote") VoteStoreDto voteStoreDto);

    public String getStatus(@Param("id") String id, @Param("date") String date);

}
