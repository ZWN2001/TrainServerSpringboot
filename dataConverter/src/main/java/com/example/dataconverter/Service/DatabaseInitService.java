package com.example.dataconverter.Service;

import com.alibaba.fastjson.JSON;
import com.example.dataconverter.Mapper.MyMapper;
import com.example.dataconverter.bean.Route;
import com.example.dataconverter.bean.RouteAtom;
import com.example.dataconverter.bean.SeatRemainKey;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DatabaseInitService {
    ///默认每车次六节车厢,一节车厢120人，每排4人
    private final Map<String, Boolean> resultMap = new HashMap<>();
    private List<Route> allTrainRoutes = new ArrayList<>();

//    private List<RouteAtom> routeAtoms = new ArrayList<>();

    private int carriageNum = 6;
    private int eachRowNum = 4;

    private int allTicketNum = 720;
    private int intseatEachLocation = 30;

    SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd");
    @Resource
    private MyMapper myMapper;
    public Map<String, Boolean> init(){
        boolean initData = initData();
        if(initData){
//            boolean seatTypeInit = initSeatType();
//            resultMap.put("seatTypeInit",seatTypeInit);
//
//            boolean carriageInit = initcarriage();
//            resultMap.put("carriageInit",carriageInit);

                boolean ticketSeatRemainInit = initTicketSeatRemain();
                resultMap.put("ticketSeatRemainInit",ticketSeatRemainInit);

            boolean ticketPriceAtomInit = initTicketPriceAtom();
            resultMap.put("ticketPriceAtomInit",ticketPriceAtomInit);

            boolean ticketRemainInit = initTicketRemain();
            resultMap.put("ticketRemainInit",ticketRemainInit);

        }else {
            resultMap.put("initData",false);
        }
        return resultMap;
    }

    private boolean initData(){
        try {
            allTrainRoutes = myMapper.getAllTrainRoutes();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean initSeatType(){
        try {
            myMapper.initSeat();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean initcarriage(){
        try {
            for (Route route:allTrainRoutes){
                if(route.getTrainRouteId().charAt(0) == 'G'){
                    myMapper.insertCarriageInfo(route.getTrainRouteId(),1);
                }else {
                    myMapper.insertCarriageInfo(route.getTrainRouteId(),4);
                }

            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    ///需要考虑怎么存座位
    private boolean initTicketSeatRemain(){
        Date date;
        SeatRemainKey  key = new SeatRemainKey();;
        Map<String, Integer> map = new HashMap<>();
        try {
            for (Route route:allTrainRoutes){
                if (myMapper.atomNum(route.getTrainRouteId()) == 0){
                    continue;
                }
                date = new Date();

                for (int i = 0; i < 10; i++) {
                    String dateString = df3.format(date);
                    for (int j = 1; j <= carriageNum; j++) {
                        for (int k = 1; k <= eachRowNum; k++) {
                            map.clear();
                            try {
                                key.setFromStationNo(myMapper.getStationNo(route.getTrainRouteId(),route.getFromStationId()));
                                key.setToStationNo(myMapper.getStationNo(route.getTrainRouteId(),route.getToStationId()));
                            }catch (Exception e){
                                System.out.println(route);
                            }

                            key.setSeat(0);
                            map.put(key.toString(),intseatEachLocation);
                            String seatRemain = JSON.toJSONString(map);
                            try{
                                myMapper.insertSeatRemain(route.getTrainRouteId(),dateString,j,k,seatRemain);
                            }catch (Exception e){
                                System.out.println(route);
                            }

                        }
                    }
                    date = getNextDay(date);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean initTicketPriceAtom(){
        List<RouteAtom> routeAtoms = new ArrayList<>();
        Random rand=new Random();
        int priceInt;
        double price;
        try {
            for (Route route:allTrainRoutes){
                if (myMapper.atomNum(route.getTrainRouteId()) == 0){
                    continue;
                }
                routeAtoms.clear();
                routeAtoms = myMapper.getRouteAtomByRouteId(route.getTrainRouteId());
                for (RouteAtom atom : routeAtoms) {
                    if (atom.getTrainRouteId().charAt(0) == 'G'){//123等
                        for (int i = 1; i <= 3; i++) {
                            priceInt = rand.nextInt(100) + 50;
                            price = priceInt * 1.0 / 10 * i;
                            try{
                                myMapper.insertTicketPriceAtom(atom.getTrainRouteId(),atom.getStationId(),atom.getStationNo(),i,price);

                            }catch (Exception e){
                                System.out.println(atom.toString());
                            }
                        }
                    }else {
                        for (int i = 4; i <= 6; i++) {
                            priceInt = rand.nextInt(100) + 50;
                            price = priceInt * 1.0 / 10 * i;
                            try{
                                myMapper.insertTicketPriceAtom(atom.getTrainRouteId(),atom.getStationId(),atom.getStationNo(),i,price);

                            }catch (Exception e){
                                System.out.println(atom.toString());
                            }
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean initTicketRemain(){
        Date date;
        List<RouteAtom> routeAtoms = new ArrayList<>();
        Random rand=new Random();
        try {
            for (Route route:allTrainRoutes){
                if (myMapper.atomNum(route.getTrainRouteId()) == 0){
                    System.out.println(route);
                    continue;
                }
                routeAtoms.clear();
                //车次的所有原子区间
                routeAtoms = myMapper.getRouteAtomByRouteId(route.getTrainRouteId());
                int average = allTicketNum /routeAtoms.size();
                int ticketNum; // 3 ~ ave
                String dateString;
                int usedTicketNum;
                boolean isG = route.getTrainRouteId().charAt(0) == 'G';
                if (isG){//123等
                    for (int i = 1; i <= 3; i++) {
                        date = new Date();
                        //1-10天
                        for (int j = 0; j < 5; j++) {
                            dateString = df3.format(date);
                            usedTicketNum = 0;
                            for (int k = 0; k< routeAtoms.size() - 1; k++) {
                                ticketNum = rand.nextInt(average + 1 ) + 2; // 3 ~ ave
                                try {
                                    myMapper.insertTicketRemain(routeAtoms.get(k).getTrainRouteId(),i,routeAtoms.get(k).getStationId(),
                                            routeAtoms.get(k).getStationNo(),dateString,ticketNum);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                usedTicketNum += ticketNum;
                            }
                            //剩下的票
                            ticketNum = allTicketNum - usedTicketNum;
                            try {
                                myMapper.insertTicketRemain(routeAtoms.get(routeAtoms.size() - 1).getTrainRouteId(),i,
                                        routeAtoms.get(routeAtoms.size() - 1).getStationId(),
                                        routeAtoms.get(routeAtoms.size() - 1).getStationNo(),dateString,ticketNum);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            date = getNextDay(date);
                        }
                    }
                }else {
                    for (int i = 4; i <= 6; i++) {
                        date = new Date();
                        for (int j = 0; j < 5; j++) {
                            dateString = df3.format(date);
                            usedTicketNum = 0;

                            for (int k = 0; k < routeAtoms.size() - 1; k++) {
                                ticketNum = rand.nextInt(average + 1 ) + 2; // 3 ~ ave
                                try{
                                    myMapper.insertTicketRemain(routeAtoms.get(k).getTrainRouteId(),i,routeAtoms.get(k).getStationId(),
                                            routeAtoms.get(k).getStationNo(),dateString,ticketNum);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                usedTicketNum += ticketNum;
                            }
                            ticketNum = allTicketNum - usedTicketNum;
                            try {
                                myMapper.insertTicketRemain(routeAtoms.get(routeAtoms.size() - 1).getTrainRouteId(),i,
                                        routeAtoms.get(routeAtoms.size() - 1).getStationId(),
                                        routeAtoms.get(routeAtoms.size() - 1).getStationNo(),dateString,ticketNum);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            date = getNextDay(date);
                        }
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error");
            return false;
        }
    }


    private static Date getNextDay(Date date) {
        //1天24小时，1小时60分钟，1分钟60秒，1秒1000毫秒
        long addTime = 24 * 60 * 60 * 1000;
        return new  Date(date.getTime() + addTime);
    }

    private int getStationNo(String trainRouteId, String stationId){
        try{
            return myMapper.getStationNo(trainRouteId,stationId);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
