package com.zwn.trainserverspringboot.command.bean;

import lombok.Builder;
import lombok.Data;

//用于在MQ中传输的消息
@Data
@Builder
public class OrderMessage {
    Order order;
    int num;
}
