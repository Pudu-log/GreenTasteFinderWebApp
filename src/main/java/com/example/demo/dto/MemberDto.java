package com.example.demo.dto;

import lombok.Data;

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
    private LocalDateTime insertDt;
    private LocalDateTime updateDt;
    private String adminYn;
}