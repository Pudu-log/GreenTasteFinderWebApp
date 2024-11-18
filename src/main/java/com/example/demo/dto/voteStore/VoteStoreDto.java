package com.example.demo.dto.voteStore;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class VoteStoreDto {
    private String store_id;
    private String id;
    private String name;
    private Timestamp vote_dt;
    private String end_yn;
}
