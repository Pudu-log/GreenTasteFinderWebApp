package com.example.demo.service.voteStore;

import com.example.demo.dao.voteStore.IVoteStoreDao;
import com.example.demo.dto.voteStore.VoteStoreDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoteStoreService {

    private final IVoteStoreDao voteStoreDao;

    public VoteStoreService(IVoteStoreDao voteStoreDao) {
        this.voteStoreDao = voteStoreDao;
    }

    public List<VoteStoreDto> getList() {
        return voteStoreDao.getList();
    }

    public List<String> getStoreId(String date, int room_code) {
        return voteStoreDao.getStoreId(date, room_code);
    }

    public List<VoteStoreDto> getListOnDate(String date, int room_code) {
        return voteStoreDao.getListOnDate(date, room_code);
    }

    public int insertVote(VoteStoreDto voteStoreDto) {
        try{
            return voteStoreDao.insertVote(voteStoreDto);
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteVote(String place_id, String id, String date) {
        voteStoreDao.deleteVote(place_id, id, date);
    }

    public String getStatus(String id, String date){
        return voteStoreDao.getStatus(id, date);
    }

}
