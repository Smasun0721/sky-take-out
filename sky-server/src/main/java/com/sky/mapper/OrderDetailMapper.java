package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderDetailMapper {


    //通过订单号查找订单详细信息
    @Select("select * from order_detail where order_id=#{number}")
    List<OrderDetail> getDetailByNumber(String number);

    void submit(ArrayList<OrderDetail> orderDetails);

    //通过订单id查找订单详细信息
    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getDetailById(Long id);
}
