package com.zwn.trainserverspringboot.rabbitmq;

import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderMessage;
import com.zwn.trainserverspringboot.command.bean.RebookOrder;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.query.mapper.OrderQueryMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class MQConsumer {

    @Resource
    private TicketCommandMapper ticketCommandMapper;
    @Resource
    private OrderQueryMapper orderQueryMapper;

    Date date = new Date();
    SimpleDateFormat timeFrtmat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat timeFrtmat2 = new SimpleDateFormat("HH:mm:ss");
    @RabbitListener(queues= "TICKET_BOOKING_QUEUE")//指明监听的是哪一个queue
    public void receive(OrderMessage orderMessage ){
        String time = timeFrtmat.format(date.getTime());
        orderMessage.getOrder().setOrderTime(time);
        try{
            ticketCommandMapper.ticketBooking(orderMessage.getOrder(),timeFrtmat2.format(date.getTime()),
                    orderMessage.getSeatLocation());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues= "TICKET_REBOOK_QUEUE")//指明监听的是哪一个queue
    public void receive(RebookOrder rebookOrder ){
        List<Order> orders = orderQueryMapper.getOrderById(rebookOrder.getOrderId());
        for (Order o : orders){
            if(Objects.equals(o.getPassengerId(), rebookOrder.getPassengerId())){
                rebookOrder.setOriginalPrice(o.getPrice());
            }
        }
        String time = timeFrtmat2.format(date.getTime());
        rebookOrder.setCreateTime(time);
        try{
            System.out.println(rebookOrder.toString());
            ticketCommandMapper.ticketRebook(rebookOrder);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
