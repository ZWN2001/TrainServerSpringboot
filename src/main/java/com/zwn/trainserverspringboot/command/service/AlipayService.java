package com.zwn.trainserverspringboot.command.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.bean.OrderStatus;
import com.zwn.trainserverspringboot.command.bean.RebookOrder;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.config.AlipayConfig;
import com.zwn.trainserverspringboot.query.bean.AtomStationKey;
import com.zwn.trainserverspringboot.query.bean.SeatBookingInfo;
import com.zwn.trainserverspringboot.query.mapper.OrderQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.PassengerQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TicketQueryMapper;
import com.zwn.trainserverspringboot.query.mapper.TrainRouteQueryMapper;
import com.zwn.trainserverspringboot.query.service.SeatQueryService;
import com.zwn.trainserverspringboot.query.service.TicketQueryService;
import com.zwn.trainserverspringboot.util.*;
import com.zwn.trainserverspringboot.util.qrcode.QrCodeResponse;
import com.zwn.trainserverspringboot.util.qrcode.QrResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AlipayService  {

    Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AlipayConfig alipayConfig;
    @Resource
    private OrderQueryMapper orderQueryMapper;
    @Resource
    private TicketCommandMapper ticketCommandMapper;
    @Resource
    private TicketQueryMapper ticketQueryMapper;
    @Resource
    private SeatQueryService seatQueryService;
    @Resource
    private TrainRouteQueryMapper trainRouteQueryMapper;
//    @Resource
//    private PassengerQueryMapper passengerQueryMapper;
//    @Resource
//    private TicketQueryService ticketQueryService;


    public Result alipay(String orderId, List<String> passengerId, int payMethod) {
        if (orderId == null || passengerId == null||passengerId.size() == 0){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        List<Order> orders  = new ArrayList<>();
        double price = 0;
        for (String pid : passengerId){
            orders = orderQueryMapper.getOrderByIdAndPid(orderId ,pid);
            for (Order order : orders){
                if (order == null){
                    continue;
                }
                price += order.getPrice();
            }
        }

        //设置支付回调时可以在request中获取的参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orders.get(0).getOrderId());
        jsonObject.put("userId", orders.get(0).getUserId());
        jsonObject.put("departureDate", orders.get(0).getDepartureDate());
        jsonObject.put("trainRouteId", orders.get(0).getTrainRouteId());
        jsonObject.put("fromStationId", orders.get(0).getFromStationId());
        jsonObject.put("toStationId", orders.get(0).getToStationId());
        jsonObject.put("seatTypeId", orders.get(0).getSeatTypeId());
        jsonObject.put("price", price);
        jsonObject.put("payType", payMethod);
        String params = jsonObject.toString();

        //设置支付参数
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setBody(params);
        model.setTotalAmount(String.valueOf(price));
        model.setOutTradeNo(orders.get(0).getOrderId());
        model.setSubject("车票");
        try{
            //获取响应二维码信息
            QrCodeResponse qrCodeResponse = qrcodePay(model);
            return Result.getResult(ResultCodeEnum.SUCCESS,qrCodeResponse.getQr_code());
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }

    public Result alipayRebook() {
        List<RebookOrder> orderList = orderQueryMapper.getRebookOrder(UserUtil.getCurrentUserId());
        if(orderList.size() == 0){
            return Result.getResult(ResultCodeEnum.BAD_REQUEST);
        }
        double price = 0;
        for (RebookOrder rebookOrder : orderList){
            price += rebookOrder.getPrice() - rebookOrder.getOriginalPrice();
        }
        if (price <= 0){
            Order order = Order.builder()
                    .trainRouteId(orderList.get(0).getTrainRouteId())
                    .fromStationId(orderList.get(0).getFromStationId())
                    .toStationId(orderList.get(0).getToStationId())
                    .departureDate(orderList.get(0).getDepartureDate())
                    .seatTypeId(orderList.get(0).getSeatTypeId()).build();
            int num =  orderList.size();
            ticketCommandMapper.updateTicketRemain(order.getTrainRouteId(),
                    order.getSeatTypeId(),order.getDepartureDate(),
                    order.getFromStationId(),order.getToStationId(), num);
            redisIncr(order, num);
            //处理状态
            for(RebookOrder o : orderList){
                ticketCommandMapper.ticketRebookPrice(o.getOrderId(),o.getPassengerId(),o.getPrice());
            }
            ticketCommandMapper.ticketRebookDone(orderList.get(0).getOrderId());
            return Result.getResult(ResultCodeEnum.SUCCESS,true);
        }
        //设置支付回调时可以在request中获取的参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", orderList.get(0).getOrderId());
        jsonObject.put("userId", orderList.get(0).getUserId());
        jsonObject.put("departureDate", orderList.get(0).getDepartureDate());
        jsonObject.put("trainRouteId", orderList.get(0).getTrainRouteId());
        jsonObject.put("fromStationId", orderList.get(0).getFromStationId());
        jsonObject.put("toStationId", orderList.get(0).getToStationId());
        jsonObject.put("seatTypeId", orderList.get(0).getSeatTypeId());
        jsonObject.put("num", orderList.size());
        jsonObject.put("price", price);
        jsonObject.put("payType", 1);
        String params = jsonObject.toString();

        //设置支付参数
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setBody(params);
        model.setTotalAmount(String.valueOf(price));
        model.setOutTradeNo(orderList.get(0).getOrderId()+1);
        model.setSubject("车票改签");
        try{
            //获取响应二维码信息
            QrCodeResponse qrCodeResponse = qrcodePay(model);
            return Result.getResult(ResultCodeEnum.SUCCESS,qrCodeResponse.getQr_code());
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }

    public boolean alipayCallback(HttpServletRequest request) {
        try {
            Map<String, String> params = ParamsUtil.ParamstoMap(request);
            logger.info("回调参数=========>" + params);
            String tradeNo = params.get("trade_no");
            String body1 = params.get("body");
            logger.info("交易的流水号和交易信息===========>"+tradeNo+"\n"+ body1);
            JSONObject body = JSONObject.parseObject(body1);
            int ptype = body.getInteger("payType");
            if(ptype == 0){
                String orderId = body.getString("orderId");
                List<SeatBookingInfo> seatBookingInfo = ticketQueryMapper.getPreferSeatBookingInfo(orderId);
                for (SeatBookingInfo info : seatBookingInfo) {
                    int num = -1;//加负数，相当于减库存
                    ticketCommandMapper.updateTicketRemain(info.getTrainRouteId(),
                            info.getSeatType(),info.getDepartureDate(),
                            info.getFromStationId(),info.getToStationId(), num);

                    int[] carriageAndSeat = seatQueryService.getCarriageAndSeat(info);
                    if (!(carriageAndSeat[0] == -1) && !(carriageAndSeat[1] == -1)){
                        ticketCommandMapper.ticketSoldInit(orderId,info.getPassengerId(),carriageAndSeat[0],carriageAndSeat[1]);
                    }
                }
                ticketCommandMapper.ticketPay(orderId, tradeNo);
            }else if (ptype == 1){
                String orderId = body.getString("orderId");
                //处理库存
                Order order = Order.builder()
                                .trainRouteId(body.getString("trainRouteId"))
                                .fromStationId(body.getString("fromStationId"))
                                .toStationId(body.getString("toStationId"))
                                .departureDate(body.getString("departureDate"))
                                .seatTypeId(body.getInteger("seatTypeId")).build();
                int num =  body.getInteger("num");
                ticketCommandMapper.updateTicketRemain(order.getTrainRouteId(),
                        order.getSeatTypeId(),order.getDepartureDate(),
                        order.getFromStationId(),order.getToStationId(), num);
                redisIncr(order, num);
                //处理状态
                List<RebookOrder> orderList = orderQueryMapper.getRebookOrder(UserUtil.getCurrentUserId());
                for(RebookOrder o : orderList){
                    ticketCommandMapper.ticketRebookPrice(o.getOrderId(),o.getPassengerId(),o.getPrice());
                }
                ticketCommandMapper.ticketRebookDone(orderId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("异常====>"+ e);
            return false;
        }
        return true;
    }

    /**
     * 支付宝客户端发送支付请求获取支付二维码信息
     */
    public QrCodeResponse qrcodePay(AlipayTradePrecreateModel model) throws AlipayApiException {
        //1.获取请求客户端
        AlipayClient alipayClient = getAlipayClient();

        //2. 获取请求对象
        AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();

        //3.设置请求参数
        alipayRequest.setBizModel(model);
        alipayRequest.setNotifyUrl(alipayConfig.getNotify_url());
        alipayRequest.setReturnUrl(alipayConfig.getReturn_url());
        AlipayTradePrecreateResponse execute;
        execute = alipayClient.execute(alipayRequest);
        String body = execute.getBody();
        logger.info("请求的响应二维码信息====>" + body);
        QrResponse qrResponse = JSON.parseObject(body, QrResponse.class);
        return qrResponse.getAlipay_trade_precreate_response();
    }

    /**
     * 获取阿里客户端
     */
    public AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(
                alipayConfig.getURL(),
                alipayConfig.getAPPID(),
                alipayConfig.getRSA_PRIVATE_KEY(),
                alipayConfig.getFORMAT(),
                alipayConfig.getCHARSET(),
                alipayConfig.getALIPAY_PUBLIC_KEY(),
                alipayConfig.getSIGNTYPE()
        );
    }


    private boolean isEnough(Order order, int ticketNum){
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        boolean enough = true;
        for (AtomStationKey atomStationKey : atomStationKeys) {
            atomStationKey.setDepartureDate(order.getDepartureDate());
            atomStationKey.setSeatTypeId(order.getSeatTypeId());
            String key = com.alibaba.fastjson2.JSON.toJSONString(atomStationKey);
            long surplusNumber = redisUtil.decr(key, ticketNum);
            redisUtil.incr(key, ticketNum);
            if (surplusNumber < 0) {
                enough = false;
                break;
            }
        }
        return enough;
    }

    ///扣库存
    private void redisDecr(Order order,int num){
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        for (AtomStationKey atomStationKey : atomStationKeys) {
            atomStationKey.setDepartureDate(order.getDepartureDate());
            atomStationKey.setSeatTypeId(order.getSeatTypeId());
            String key = com.alibaba.fastjson2.JSON.toJSONString(atomStationKey);
            redisUtil.decr(key, num);
        }
    }

    ///加库存
    private void redisIncr(Order order, int num){
        List<AtomStationKey> atomStationKeys = trainRouteQueryMapper.getAtomStationKeys(order);
        for (AtomStationKey atomStationKey : atomStationKeys) {
            atomStationKey.setDepartureDate(order.getDepartureDate());
            atomStationKey.setSeatTypeId(order.getSeatTypeId());
            String key = com.alibaba.fastjson2.JSON.toJSONString(atomStationKey);
            redisUtil.incr(key, num);
        }
    }
}
