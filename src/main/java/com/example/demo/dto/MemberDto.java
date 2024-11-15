package com.example.demo.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MemberDto {
    private String id;
    private String pw;
    private String name;
    private Integer roomCode;
    private String email;
    private Date birth;
    private Timestamp insertDt;
    private Timestamp  updateDt;
    private String adminYn;
}