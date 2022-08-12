package com.zwn.trainserverspringboot.command.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pay {
    private String orderId;
    private List<String> passengerId;
    private Integer payMethod;
}
