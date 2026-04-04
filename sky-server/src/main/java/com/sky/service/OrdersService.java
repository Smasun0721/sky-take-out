package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrdersService {

    //订单条件分页查询
    PageResult page(OrdersPageQueryDTO ordersPageQueryDTO);

    //各个状态的订单数量统计
    OrderStatisticsVO statistics();

    //查询订单详情
    OrderVO queryDetailById(Long id);

    //接单
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    //拒单
    void reject(OrdersRejectionDTO ordersRejectionDTO);

    //取消订单
    void cancel(OrdersCancelDTO ordersCancelDTO);

    //派送订单
    void deliver(Long id);

    //完成订单
    void complete(Long id);

    //历史订单查询
    PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    //根据订单id取消订单
    void cancelById(Long id);

    //再来一单
    void again(Long id);

    //催单
    void reminder(Long id);
}
