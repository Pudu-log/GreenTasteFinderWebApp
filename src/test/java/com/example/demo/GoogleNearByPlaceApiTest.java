package com.example.demo;

import com.example.demo.model.Restaurant;
import com.example.demo.utils.GoogleNearByPlaceApi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GoogleNearByPlaceApiTest {

    @Autowired
    private GoogleNearByPlaceApi googleNearByPlaceApi;

    @Test
    void testFetchNearbyRestaurants() {

    }
}
