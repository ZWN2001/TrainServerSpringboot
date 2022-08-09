package com.zwn.trainserverspringboot.command.controller;

import com.zwn.trainserverspringboot.command.bean.Order;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class TicketCommandControllerTest {
    @Autowired
    TicketCommandController ticketCommandController;
    Order order =   Order.builder()
            .orderId(null)
            .departureDate("2022-08-09")
            .fromStationId("BJP")
            .toStationId("YBP")
            .passengerId(null)
            .userId(15866554038L)
            .seatTypeId(1)
            .trainRouteId("1461")
            .build();
    List<String> passengers = new ArrayList<>();

//    @BeforeAll
//    static void login(){
//        UserController userController = new UserController();
//        userController.login(15866554038L,"12345678");
//    }

    @Test
    void ticketsBooking() {
        passengers.add("370782200112215512");
        passengers.add("37078220011221551X");
        System.out.println(ticketCommandController.ticketsBooking(order,passengers));
    }

    @Test
    void ticketRefund() {
        System.out.println(ticketCommandController.ticketRefund("2022080910353701"));
    }

    @Test
    void ticketRebook() {
    }
}