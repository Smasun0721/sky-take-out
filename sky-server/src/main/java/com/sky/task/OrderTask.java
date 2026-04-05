package com.sky.task;


import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    //检测待支付超时订单
    @Scheduled(cron = "0 * * * * *")
    public void processPayTimeoutOrder() {
        log.info("检测待支付超时订单:{}", LocalDateTime.now());
        List<Orders> timeoutOrders = orderMapper.processTimeoutOrder(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));
        if (timeoutOrders != null && !timeoutOrders.isEmpty()) {
            for (Orders orders : timeoutOrders) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时,自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    //检测一直未完成派送的订单
    @Scheduled(cron = "* * 1 * * * ")
    public void processDeliveryOrder(){
        log.info("检测一直未完成派送的订单:{}",LocalDateTime.now());
        List<Orders> timeoutOrders = orderMapper.processTimeoutOrder(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(60));
        if (timeoutOrders != null && !timeoutOrders.isEmpty()) {
            for (Orders orders : timeoutOrders) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
