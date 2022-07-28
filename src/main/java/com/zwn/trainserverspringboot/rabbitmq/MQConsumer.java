package com.zwn.trainserverspringboot.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MQConsumer {

//    @Resource
//    //private OrderMapper orderMapper;
//    private GoodsOrderMapper goodsOrderMapper;
//
//    @Resource
//    private GoodsMapper goodsMapper;
//
//
//    @RabbitListener(queues= "ORDER_QUEUE")//指明监听的是哪一个queue
//    public void receive(GoodsOrder order ){
//        long now = System.currentTimeMillis();
//        System.out.println("RabbitMQ: receiving message。。。。");
//        goodsOrderMapper.addOrder(order);
//        // 更新库存
//        goodsMapper.updateGoodsNum(order);
//        System.out.println("RabbitMQ: cost time："+(System.currentTimeMillis()-now)+"ms");
//    }

}
