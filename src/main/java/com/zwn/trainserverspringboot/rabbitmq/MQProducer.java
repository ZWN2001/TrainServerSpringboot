package com.zwn.trainserverspringboot.rabbitmq;

import com.zwn.trainserverspringboot.command.bean.OrderMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MQProducer {
    @Resource
    private AmqpTemplate amqpTemplate;
    //Direct模式
    public void sendTicketBooking(OrderMessage orderMessage) {
        //第一个参数队列的名字，第二个参数发出的信息
        amqpTemplate.convertAndSend("TICKET_BOOKING_QUEUE", orderMessage);
    }

}
