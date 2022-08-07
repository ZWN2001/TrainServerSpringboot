package com.zwn.trainserverspringboot.util.qrcode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrResponse {

    private QrCodeResponse alipay_trade_precreate_response;

    private String sign;
}
