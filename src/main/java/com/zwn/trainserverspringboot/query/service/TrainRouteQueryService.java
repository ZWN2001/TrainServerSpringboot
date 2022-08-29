package com.zwn.trainserverspringboot.query.service;

import com.zwn.trainserverspringboot.query.bean.*;
import com.zwn.trainserverspringboot.query.mapper.StationQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TicketQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TrainRouteQueryMapper;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class TrainRouteQueryService {

    @Resource
    private StationQueryMapper stationQueryMapper;

    @Resource
    TrainRouteQueryMapper trainRouteQueryMapper;

    @Resource
    private TicketQueryMapper ticketQueryMapper;

    public Result querytrainRoute(String from, String to,  String date){
        int day = Integer.parseInt(date.substring(8,10)) - 1;
        //������ʵӦ�ö����ڽ���У���
        if (day > 30 || day < 0){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        List<TrainRoute> trainRoutes = new ArrayList<>();
        TicketRouteTimeInfo ticketRouteTimeInfo;
        List<TicketsRemain> ticketsRemain;
        Map<Integer,Integer> tickets;
        //�������е����г�վ
        List<String> allFromStations = stationQueryMapper.getSameCityStationId(from);
        List<String> allToStations = stationQueryMapper.getSameCityStationId(to);

        for (String fromStationId : allFromStations) {
            for (String toStationId : allToStations){
                List<TrainRoute> trainRoute = trainRouteQueryMapper.getTrainRoutesByFromAndTo(fromStationId, toStationId);
                if(trainRoute.size() == 0){
                    continue;
                }
                //����Ʊ�ƻ�
                trainRoute.removeIf(route -> trainRouteQueryMapper.getRunPlan(route.getTrainRouteId()).getRunPlan().charAt(day) == '0');

                for (TrainRoute route : trainRoute){
                    //����ʱ��
                    ticketRouteTimeInfo = (TicketRouteTimeInfo) queryTicketRouteTimeInfo(route.getTrainRouteId(),
                            route.getFromStationId(),route.getToStationId()).getData();
                    route.setStartTime(ticketRouteTimeInfo.getStartTime());
                    route.setArriveTime(ticketRouteTimeInfo.getArriveTime());
                    route.setDurationInfo(ticketRouteTimeInfo.getDurationInfo());
                    //����Ʊ��
                    tickets = new HashMap<>();
                    int ticketsNum = 0;
                    ticketsRemain = ticketQueryMapper.getTicketsRemain(route.getTrainRouteId(), date,
                            route.getFromStationId(), route.getToStationId());
                    for (TicketsRemain t : ticketsRemain) {
                        ticketsNum += t.getRemainingTicketNum();
                        tickets.put(t.getSeatTypeId(),t.getRemainingTicketNum());
                    }

                    if(ticketsNum == 0){
                        trainRoute.remove(route);
                    }else {
                        route.setTickets(tickets);
                    }
                }
                trainRoutes.addAll(trainRoute);
            }
        }
        return Result.getResult(ResultCodeEnum.SUCCESS,trainRoutes);
    }

    public Result querytrainRouteTransfer(String from, String to, String date){
        int Max = 12;//��ȡ�� 12 ���ֹͣ
        int day = Integer.parseInt(date.substring(8,10)) - 1;
        //������ʵӦ�ö����ڽ���У���
        if (day > 30 || day < 0){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        TicketRouteTimeInfo ticketRouteTimeInfo;
        List<TicketsRemain> ticketsRemain;
        Map<Integer,Integer> tickets = new HashMap<>();
        List<String> allFromStationIds = stationQueryMapper.getSameCityStationId(from);
        List<String> allToStationIds = stationQueryMapper.getSameCityStationId(to);
        List<String> possibleStartTrainRouteIds;
        List<String> possibleTransStationIds;
        List<TrainRoute> trainRoutes = new ArrayList<>();
        List<TrainRouteTransfer> result = new ArrayList<>();
        boolean content = false;
        TrainRouteTransfer resultEach;
        try {
            for(String fromStationId : allFromStationIds){//����ͬ�ǵĳ���վ
                possibleStartTrainRouteIds = trainRouteQueryMapper.getStationContainRoute(fromStationId);
                //ȥ��ʵ���ϲ����۵ĳ���
                possibleStartTrainRouteIds.removeIf(route -> trainRouteQueryMapper.getRunPlan(route).getRunPlan().charAt(day) == '0');
                for(String possibleStartTrainRouteId : possibleStartTrainRouteIds){//���п��ܵĵ�һ�˳���
                    possibleTransStationIds = trainRouteQueryMapper.getStationInTrainRouteAfter(possibleStartTrainRouteId,fromStationId);
                    for(String possibleTransStationId : possibleTransStationIds){//���ܵ���ת�ĳ�վ
                        trainRoutes.clear();
                        for(String toStationId : allToStationIds){
                            trainRoutes.addAll(trainRouteQueryMapper.getTrainRoutesByFromAndTo(possibleTransStationId, toStationId));
                        }
                        if(trainRoutes.size()>0){
                            trainRoutes.removeIf(route -> trainRouteQueryMapper.getRunPlan(route.getTrainRouteId()).getRunPlan().charAt(day) == '0');
                            for (TrainRoute trainRoute : trainRoutes){
                                if(!Objects.equals(trainRoute.getTrainRouteId(), possibleStartTrainRouteId)){//����ʵ������ֱ������
                                    if(result.size() < Max){
                                        resultEach = new TrainRouteTransfer();
                                        resultEach.setNextRouteInfo(trainRoute);
                                        resultEach.setTrainRouteId1(possibleStartTrainRouteId);
                                        resultEach.setFromStationId(fromStationId);
                                        ticketRouteTimeInfo = (TicketRouteTimeInfo) queryTicketRouteTimeInfo(
                                                possibleStartTrainRouteId, fromStationId, possibleTransStationId).getData();
                                        resultEach.setStartTimeFrom(ticketRouteTimeInfo.getStartTime());
                                        resultEach.setArriveTimeTrans(ticketRouteTimeInfo.getArriveTime());

                                        resultEach.caculateDuration();
                                        //ȥ������ʱ��̫�̻�̫��
                                        if(resultEach.getDurationTransfer() < 15 || resultEach.getDurationTransfer() > 360){
                                            continue;
                                        }
                                        for(TrainRouteTransfer trainRouteTransfer : result){
                                            if (resultEach.equals(trainRouteTransfer)){
                                                content = true;
                                                break;
                                            }
                                        }
                                        if(content){continue;}

                                        //��ʼ����һ�׶ε�Ʊ
                                        tickets.clear();
                                        int ticketsNum = 0;
                                        ticketsRemain = ticketQueryMapper.getTicketsRemain(resultEach.getTrainRouteId1(), date,
                                                resultEach.getFromStationId(), resultEach.getTransStationId());
                                        for (TicketsRemain t : ticketsRemain) {
                                            ticketsNum += t.getRemainingTicketNum();
                                            tickets.put(t.getSeatTypeId(),t.getRemainingTicketNum());
                                        }
                                        if(ticketsNum == 0){
                                            continue;
                                        }else {
                                            resultEach.setTicketsFirst(tickets);
                                            //��ʼ���ڶ��׶ε�Ʊ
                                            ticketsNum = 0;
                                            tickets.clear();
                                            ticketsRemain = ticketQueryMapper.getTicketsRemain(resultEach.getTrainRouteId2(), date,
                                                    resultEach.getTransStationId(), resultEach.getToStationId());
                                            for (TicketsRemain t : ticketsRemain) {
                                                ticketsNum += t.getRemainingTicketNum();
                                                tickets.put(t.getSeatTypeId(),t.getRemainingTicketNum());
                                            }
                                            if(ticketsNum == 0){
                                                continue;
                                            }else {
                                                resultEach.setTicketsNext(tickets);
                                            }
                                        }
                                        result.add(resultEach);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }

        return Result.getResult(ResultCodeEnum.SUCCESS, result);
    }

    public Result queryTrainRouteDetail(String train_route_id){
        List<TrainRouteAtom> trainRouteAtoms = trainRouteQueryMapper.queryTrainRouteDetail(train_route_id);
        return Result.getResult(ResultCodeEnum.SUCCESS,trainRouteAtoms);
    }

    public Result queryTicketRouteTimeInfo(String trainRouteId, String fromStationId, String toStationId){
        List<TrainRouteAtom> atomList = trainRouteQueryMapper.queryTrainRouteInfo(trainRouteId,fromStationId,toStationId);
        if (atomList.size() == 0){
            return Result.getResult(ResultCodeEnum.TRAIN_ROUTE_NOT_EXIST);
        }else {
            TicketRouteTimeInfo ticketRouteTimeInfo = new TicketRouteTimeInfo();
            ticketRouteTimeInfo.setStartTime(atomList.get(0).getStartTime());
            ticketRouteTimeInfo.setArriveTime(atomList.get(atomList.size() - 1).getArriveTime());
            int day = 0;
            String hourAndMinute;
            for (int i = 1; i < atomList.size(); i++) {
                if (isTimeReduce(atomList.get(i - 1).getStartTime(),atomList.get(i).getArriveTime())){
                    day++;
                }
            }
            //����ʱ���Сʱ�ȵ����СʱҪ�󣨵��������Ϊ������
            if (isTimeReduce(atomList.get(0).getStartTime(),atomList.get(atomList.size() - 1).getArriveTime())){
                day--;//��Ҫ��һ����м���
                hourAndMinute = getHourAndMinute2(atomList.get(0).getStartTime(),atomList.get(atomList.size() - 1).getArriveTime());
            }else {
                hourAndMinute = getHourAndMinute1(atomList.get(0).getStartTime(),atomList.get(atomList.size() - 1).getArriveTime());
            }
            String duration = day > 0 ? day + ":" + hourAndMinute : hourAndMinute;
            ticketRouteTimeInfo.setDurationInfo(duration);
            return Result.getResult(ResultCodeEnum.SUCCESS,ticketRouteTimeInfo);
        }
    }

    private boolean isTimeReduce(String first, String next){
        return Integer.parseInt(first.substring(0, 2)) > Integer.parseInt(next.substring(0, 2));
    }

    //first < next, ��first����next
    private String getHourAndMinute1(String first, String next){
        int allMinutes = 60 * (Integer.parseInt(next.substring(0, 2)) - Integer.parseInt(first.substring(0, 2)))
                + (Integer.parseInt(next.substring(3, 5)) - Integer.parseInt(first.substring(3, 5)));
        return allMinutes/60 + ":" + allMinutes % 60;
    }

    //������������ʵ
//           60 * ((24 - int.parse(first.substring(0, 2)) + int.parse(next.substring(0, 2))) % 24)
//            + ((60 - int.parse(next.substring(3, 5)) + int.parse(first.substring(3, 5))) % 60);

    private String getHourAndMinute2(String first, String next){
        int allMinutes = 60 * (Integer.parseInt(first.substring(0, 2)) - Integer.parseInt(next.substring(0, 2)))
                + (Integer.parseInt(first.substring(3, 5)) - Integer.parseInt(next.substring(3, 5)));
        allMinutes = 24 * 60 - allMinutes;
        return allMinutes/60 + ":" + allMinutes % 60;
    }
}
