package com.zwn.trainserverspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class RedisInit {
//    @Autowired
//    private GoodsService goodsService;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    @Override
//    public void run(String... args) {
//
//        List<Goods> goodsList = goodsService.queryAllGoods();
//
//        // 将商品以<name，num> 的形式存入redis
//        goodsList.forEach(x -> {
//            redisUtil.set(x.getGoodsName(), x.getGoodsNum());
//        } );
//        System.out.println("=============>>> Redis init done……");
//    }
}
