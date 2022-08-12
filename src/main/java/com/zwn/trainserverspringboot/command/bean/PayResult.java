package com.zwn.trainserverspringboot.command.bean;

import com.zwn.trainserverspringboot.util.Result;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PayResult {
    List<String> results;
    byte[] qrcode;
}
