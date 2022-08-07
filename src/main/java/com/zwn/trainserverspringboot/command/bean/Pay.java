package com.zwn.trainserverspringboot.command.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pay {

    private String orderId;
    private double price;
    private Integer payMethod;

}
