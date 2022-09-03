package com.zwn.trainserverspringboot.query.service;

import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.mapper.SeatCommandMapper;
import com.zwn.trainserverspringboot.query.bean.SeatBookingInfo;
import com.zwn.trainserverspringboot.query.bean.SeatRemainKey;
import com.zwn.trainserverspringboot.query.bean.SeatType;
import com.zwn.trainserverspringboot.query.mapper.CarriageQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.SeatQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TrainRouteQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeatQueryService {
    @Resource
    private SeatQueryMapper seatTypeQueryMapper;
    @Resource
    private CarriageQueryMapper carriageQueryMapper;
    @Resource
    private TrainRouteQueryMapper trainRouteQueryMapper;
    @Resource
    private SeatCommandMapper seatCommandMapper;

    public Result getAllSeatType(){
        List<SeatType> result;
        try{
            result = seatTypeQueryMapper.getAllSeatType();
            return Result.getResult(ResultCodeEnum.SUCCESS,result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }

    //分配车厢、座位
    public int[] getCarriageAndSeat(SeatBookingInfo seatBookingInfo){
        int[] result = {-1,-1};
        boolean got = false;//是否完成分配
        try {
            SeatRemainKey passengerKey = trainRouteQueryMapper.getFromToNo(seatBookingInfo.getTrainRouteId(),
                    seatBookingInfo.getFromStationId(),seatBookingInfo.getToStationId());
            List<Integer> carriageIds = carriageQueryMapper.getCarriageIds(seatBookingInfo.getTrainRouteId(),
                    seatBookingInfo.getSeatType());
            System.out.println("carriageIds:");
            System.out.println(carriageIds);
            for (Integer carriageId : carriageIds) {
                String remainString = seatTypeQueryMapper.getSeatRemain(seatBookingInfo.getTrainRouteId(),
                        seatBookingInfo.getDepartureDate(),
                        carriageId, seatBookingInfo.getSeatBooking());
                System.out.println(remainString);

                Map map = JSON.parseObject(remainString, Map.class);
                Map<String, Integer> remainMap = new HashMap<>();

                int value;
                int index = 0;
                for (Object obj : map.keySet()) {
                    value = (int) map.get(obj);
                    remainMap.put(obj.toString(), value);
                }
//                System.out.println(remainMap);
                for (String keyString : remainMap.keySet()) {
                    SeatRemainKey key = SeatRemainKey.fromString(keyString);
                    value = remainMap.get(keyString);
                    assert key != null;
                    if (key.getFromStationNo() <= passengerKey.getFromStationNo() &&
                              passengerKey.getToStationNo() <= key.getToStationNo()) {
                        //满足条件，分配此座位
                        got = true;
                        result[0] = carriageId;//车厢号
                        int seat;
                        if(key.getSeat() != 0){
                            seat = key.getSeat();
                        }else {
                            int rowNum = index / 2 ;//列的哪一排
                            seat = seatBookingInfo.getSeatBooking() + 4 * rowNum;
                        }
                        result[1] = seat;//分配座号
                        if(key.getFromStationNo() != passengerKey.getFromStationNo()){
                            SeatRemainKey newFromKey = new SeatRemainKey(key.getFromStationNo(), passengerKey.getFromStationNo(),seat);
                            String newFromKeyString = newFromKey.toString();
                            if (remainMap.containsKey(newFromKeyString)) {
                                int num = remainMap.get(newFromKeyString) + 1;
                                remainMap.remove(newFromKeyString);
                                remainMap.put(newFromKeyString, num);
                            } else {
                                remainMap.put(newFromKeyString, 1);
                            }
                        }
                        if(passengerKey.getToStationNo() != key.getToStationNo()){
                            SeatRemainKey newToKey = new SeatRemainKey(passengerKey.getToStationNo(), key.getToStationNo(), seat);
                            String newToKeyString = newToKey.toString();
                            if (remainMap.containsKey(newToKeyString)) {
                                int num = remainMap.get(newToKeyString) + 1;
                                remainMap.remove(newToKeyString);
                                remainMap.put(newToKeyString, num);
                            } else {
                                remainMap.put(newToKeyString, 1);
                            }
                        }

                        remainMap.remove(keyString);
                        if (value != 1) {
                            remainMap.put(keyString, value - 1);
                        }
                        break;
                    } else {
                        //不满足条件,略过
                        index = index + value;
                    }
                }
                if (got) {
                    //做出了更改才去更新数据库
                    String seatRemainString = JSON.toJSONString(remainMap);
                    System.out.println("更新："+seatRemainString);
                    seatCommandMapper.updateSeatRemain(seatBookingInfo.getTrainRouteId(),
                            seatBookingInfo.getDepartureDate(),
                            carriageId, seatBookingInfo.getSeatBooking(), seatRemainString);
                    break;
                }
            }
            return result;
            //可能存在所有车厢都没有可分配的情况
        }catch (Exception e){
            e.printStackTrace();
            return result;
        }
    }
}
