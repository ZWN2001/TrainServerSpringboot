//package com.zwn.trainserverspringboot;
//
//import com.alibaba.fastjson2.JSON;
//import com.zwn.trainserverspringboot.query.bean.AtomStationMap;
//import com.zwn.trainserverspringboot.query.mapper.TrainRouteQueryMapper;
//import com.zwn.trainserverspringboot.util.RedisUtil;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@Component
//@Order(1)
//public class RedisInit implements CommandLineRunner {
//    @Resource
//    private TrainRouteQueryMapper trainRouteQueryMapper;
//
//    @Resource
//    private RedisUtil redisUtil;
//
//    @Override
//    public void run(String... args) {
//        List<AtomStationMap> AtomStationMaps = trainRouteQueryMapper.getAllAtomStationKeys();
//        AtomStationMaps.forEach(x -> {
//            String key = JSON.toJSONString(x.getKey());
//            redisUtil.set(key, x.getValue());
//        } );
//        System.out.println("=============>>> Redis init done……");
//    }
//}
