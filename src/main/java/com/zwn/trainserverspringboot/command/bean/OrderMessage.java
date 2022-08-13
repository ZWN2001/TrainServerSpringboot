package com.zwn.trainserverspringboot.command.bean;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

//用于在MQ中传输的消息
@Data
@Builder
public class OrderMessage  implements Serializable {
    Order order;
    int num;
}
