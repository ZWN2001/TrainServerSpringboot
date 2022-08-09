package com.zwn.trainserverspringboot.rabbitmq;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderMessage;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MQConsumer {

    @Resource
    private TicketCommandMapper ticketCommandMapper;

    Date date = new Date();
    SimpleDateFormat timeFrtmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @RabbitListener(queues= "TICKET_BOOKING_QUEUE")//指明监听的是哪一个queue
    public void receive(OrderMessage orderMessage ){
        String time = timeFrtmat.format(date.getTime());
        orderMessage.getOrder().setOrderTime(time);
        ticketCommandMapper.ticketBooking(orderMessage.getOrder());
        ticketCommandMapper.updateTicketRemain(orderMessage.getOrder().getTrainRouteId(),
                orderMessage.getOrder().getSeatTypeId(),orderMessage.getOrder().getDepartureDate(),
                orderMessage.getOrder().getFromStationId(),orderMessage.getOrder().getToStationId(),
                orderMessage.getNum());
    }

}
