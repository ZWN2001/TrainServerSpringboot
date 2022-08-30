package com.zwn.trainserverspringboot.command.controller;


import com.alibaba.fastjson.JSONObject;
import com.zwn.trainserverspringboot.command.bean.Order;
import com.zwn.trainserverspringboot.command.service.AlipayService;
import com.zwn.trainserverspringboot.util.Result;
import com.zwn.trainserverspringboot.util.ResultCodeEnum;
import com.zwn.trainserverspringboot.util.StringUtil;
import com.zwn.trainserverspringboot.util.UserCheck;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AlipayController {

    @Resource
    private AlipayService alipayService;

    /**
     * 拉起支付请求
     */
    @GetMapping("/alipay/pay")
    public Result alipay(String orderId, String passengerIdString, int payMethod) {
        List<String> pids = StringUtil.getListFromString(passengerIdString);
        try {
            return alipayService.alipay(orderId,pids,payMethod);
        }catch (Exception e){
            e.printStackTrace();
            return Result.getResult(ResultCodeEnum.BAD_REQUEST,e.getClass().toString());
        }
    }

    @GetMapping("/alipay/payRebook")
    public Result alipayRebook(){
        Result result = UserCheck.check();
        if (result.getCode() == ResultCodeEnum.SUCCESS.getCode()){
            try{
                return alipayService.alipayRebook();
            }catch (Exception e){
                e.printStackTrace();
                return Result.getResult(ResultCodeEnum.BAD_REQUEST);
            }
        }else {
            return result;
        }
    }

    /**
     * 支付成功以后的异步回调API
     */
    @RequestMapping("/alipay/pay/callback")
    public String  notify_url(HttpServletRequest request) {
        boolean result =  alipayService.alipayCallback(request);
        if(result){
            return "success";
        }else{
            return "false";
        }
    }
}
