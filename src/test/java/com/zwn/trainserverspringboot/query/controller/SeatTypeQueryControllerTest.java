package com.zwn.trainserverspringboot.query.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SeatTypeQueryControllerTest {

    @Resource
    private SeatTypeQueryController seatTypeQueryController;

    @Test
    void getAllSeatType() {
        System.out.println(seatTypeQueryController.getAllSeatType());
    }
}