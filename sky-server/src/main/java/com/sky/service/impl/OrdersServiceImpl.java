package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.OrdersService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ShoppingCartService shoppingCartService;

    //订单条件分页查询
    @Override
    public PageResult page(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = ordersMapper.page(ordersPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //各个状态的订单数量统计
    @Override
    public OrderStatisticsVO statistics() {
        OrderStatisticsVO orderStatisticsVO = ordersMapper.statistics();
        return orderStatisticsVO;
    }

    //查询订单详情
    @Override
    public OrderVO queryDetailById(Long id) {
        OrderVO orderVO = new OrderVO();
        Orders orders = orderMapper.getById(id);     //通过id查找订单信息
        BeanUtils.copyProperties(orders, orderVO);
        List<OrderDetail> list = orderDetailMapper.getDetailById(id);    //通过订单id查找订单详细信息
        orderVO.setOrderDetailList(list);
        return orderVO;
    }

    //接单
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        ordersConfirmDTO.setStatus(3);  //将订单状态设置成已接单
        ordersMapper.confirm(ordersConfirmDTO);
    }

    //拒单
    @Override
    public void reject(OrdersRejectionDTO ordersRejectionDTO) {
        ordersMapper.reject(ordersRejectionDTO);
    }

    //取消订单
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        ordersMapper.cancel(ordersCancelDTO);
    }

    //派送订单
    @Override
    public void deliver(Long id) {
        ordersMapper.deliver(id);
    }

    //完成订单
    @Override
    public void complete(Long id) {
        ordersMapper.complete(id);
    }

    //用户历史订单查询
    @Override
    public PageResult historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        Page<Orders> page = ordersMapper.page(ordersPageQueryDTO);
        List<OrderVO> list = new ArrayList<>();
        for (Orders orders : page.getResult()) {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(orders, orderVO);
            List<OrderDetail> details = orderDetailMapper.getDetailById(orders.getId());
            orderVO.setOrderDetailList(details);
            list.add(orderVO);
        }
        return new PageResult(page.getTotal(), list);
    }

    //用户根据订单id取消订单
    @Override
    public void cancelById(Long id) {
        OrdersCancelDTO ordersCancelDTO = new OrdersCancelDTO();
        ordersCancelDTO.setCancelReason("用户取消");
        ordersCancelDTO.setId(id);
        ordersMapper.cancel(ordersCancelDTO);
    }

    //再来一单
    @Override
    public void again(Long id) {
        //获取上一单的购物车信息
        List<OrderDetail> orderDetails = orderDetailMapper.getDetailById(id);
        for(OrderDetail orderDetail:orderDetails){
            ShoppingCartDTO shoppingCartDTO=new ShoppingCartDTO();
            BeanUtils.copyProperties(orderDetail,shoppingCartDTO);
            shoppingCartService.add(shoppingCartDTO);
        }

        //提交订单信息
        Orders order = ordersMapper.getById(id);
        OrdersSubmitDTO ordersSubmitDTO = new OrdersSubmitDTO();
        BeanUtils.copyProperties(order, ordersSubmitDTO);
        orderService.submit(ordersSubmitDTO);
    }

    //催单
    @Override
    public void reminder(Long id) {

    }
}
