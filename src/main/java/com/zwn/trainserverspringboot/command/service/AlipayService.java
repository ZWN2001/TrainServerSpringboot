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
import com.zwn.trainserverspringboot.command.bean.PayResult;
import com.zwn.trainserverspringboot.command.mapper.TicketCommandMapper;
import com.zwn.trainserverspringboot.config.AlipayConfig;
import com.zwn.trainserverspringboot.query.mapper.OrderQueryMapper;
import com.zwn.trainserverspringboot.util.ParamsUtil;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.qrcode.QRCodeUtil;
import com.zwn.trainserverspringboot.util.qrcode.QrCodeResponse;
import com.zwn.trainserverspringboot.util.qrcode.QrResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AlipayService  {

    Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Resource
    private AlipayConfig alipayConfig;

    @Resource
    private OrderQueryMapper orderQueryMapper;

    @Resource
    private TicketCommandMapper ticketCommandMapper;


    public Result alipay(String orderId, List<String> passengerId, int payMethod) throws Exception {
        Order order = new Order();
        double price = 0;
        List<String> errorList = new ArrayList<>();
        for (String pid : passengerId){
             order = orderQueryMapper.getOrderById(orderId ,pid);
            if (order == null){
                errorList.add(pid);
            }
            assert order != null;
            price += order.getPrice();
        }
        order.setPrice(price);


        //设置支付回调时可以在request中获取的参数
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("orderId", order.getOrderId());
        jsonObject.put("userId", order.getUserId());
        jsonObject.put("departureDate", order.getDepartureDate());
        jsonObject.put("trainRouteId", order.getTrainRouteId());
        jsonObject.put("fromStationId", order.getFromStationId());
        jsonObject.put("toStationId", order.getToStationId());
        jsonObject.put("seatTypeId", order.getSeatTypeId());
        jsonObject.put("price", price);
        jsonObject.put("payType", payMethod);
        String params = jsonObject.toString();

        //设置支付参数
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setBody(params);
        model.setTotalAmount(String.valueOf(order.getPrice()));
        model.setOutTradeNo(order.getOrderId());
        model.setSubject("车票");
        try{
            //获取响应二维码信息
            QrCodeResponse qrCodeResponse = qrcodePay(model);
//            //制作二维码并且返回给前端
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            String logopath;
//            logopath = ResourceUtils.getFile("classpath:favicon.png").getAbsolutePath();
//            System.out.println("二维码的图片路径为===>" + logopath);
//            BufferedImage encode = QRCodeUtil.encode(qrCodeResponse.getQr_code(), logopath, false);
//            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
//            ImageIO.write(encode, "JPEG", imageOutputStream);
//            imageOutputStream.close();
//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//            PayResult result =  PayResult.builder()
//                    .results(errorList)
//                    .qrcode(FileCopyUtils.copyToByteArray(byteArrayInputStream))
//                    .build();
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
            String ptype = body.getString("payType");
            String orderId = body.getString("orderId");
            ticketCommandMapper.ticketPay(orderId, tradeNo);

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
}
