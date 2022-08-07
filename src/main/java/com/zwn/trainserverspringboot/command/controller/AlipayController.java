package com.zwn.trainserverspringboot.command.controller;


import com.alibaba.fastjson.JSON;
import com.zwn.trainserverspringboot.command.bean.Pay;
import com.zwn.trainserverspringboot.command.service.AlipayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AlipayController {

    @Resource
    private AlipayService alipayService;

    /**
     * 拉起支付请求
     */
    @GetMapping("/alipay/pay")
    public String alipay(Pay pay) throws Exception{
        return JSON.toJSONString(alipayService.alipay(pay));
    }

    /**
     * 支付成功以后的异步回调API
     */
    @RequestMapping("/alipay/pay/callback")
    public String  notify_url(HttpServletRequest request) throws Exception{
         Boolean result =  alipayService.alipayCallback(request);
        if(result){
            return "success";
        }else{
            return "false";
        }
    }
}
