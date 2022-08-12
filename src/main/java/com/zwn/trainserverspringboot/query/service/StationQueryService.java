package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.bean.Station;
import com.zwn.trainserverspringboot.query.mapper.StationQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StationQueryService {
    @Resource
    private StationQueryMapper stationQueryMapper;

    public Result getAllStationInfo(){
        List<Station> result;
        try {
            result = stationQueryMapper.getAllStationInfo();
            return Result.getResult(ResultCodeEnum.SUCCESS,result);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.UNKNOWN_ERROR,e.getClass().toString());
        }
    }
}
