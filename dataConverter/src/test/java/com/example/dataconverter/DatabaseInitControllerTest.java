package com.example.dataconverter;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DatabaseInitControllerTest {
    @Resource
    private DatabaseInitController controller;

    @Test
    void initSuccess() {
        System.out.println(controller.initSuccess());
    }
}