package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrdersMapper {


    //条件分页查询订单
    Page<Orders> page(OrdersPageQueryDTO ordersPageQueryDTO);

    //各个状态的订单数量统计
    OrderStatisticsVO statistics();

    //接单
    @Update("update orders set status=#{status} where id=#{id}")
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    //通过订单id查找订单
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    //拒单
    @Update("update orders set status=6,rejection_reason=#{rejectionReason} where id=#{id}")
    void reject(OrdersRejectionDTO ordersRejectionDTO);

    //取消订单
    @Update("update orders set status=6,cancel_reason=#{cancelReason} where id=#{id}")
    void cancel(OrdersCancelDTO ordersCancelDTO);

    //派送订单
    @Update("update orders set status=4 where id=#{id}")
    void deliver(Long id);

    //完成订单
    @Update("update orders set status=5 where id=#{id}")
    void complete(Long id);
}
