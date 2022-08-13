package com.zwn.trainserverspringboot.query.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class TicketQueryMapperTest {

    @Resource
    private TicketQueryMapper ticketQueryMapper;
    @Test
    void getTicketToPayNum() {
        System.out.println(ticketQueryMapper.getTicketToPayNum(15866554038L));
    }
}