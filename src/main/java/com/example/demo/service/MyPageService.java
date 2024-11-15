package com.example.demo.service;

import com.example.demo.dao.IMyPageActDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {
    @Autowired
    IMyPageActDao myPageActDao;
}
