package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.mapper.SeatTypeQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class SeatTypeQueryService {
    @Resource
    private SeatTypeQueryMapper seatTypeQueryMapper;

    public Result getAllSeatType(){
        Map<Integer,String> result;
        try{
            result = seatTypeQueryMapper.getAllSeatType();
            return Result.getResult(ResultCodeEnum.SUCCESS,result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }
}
