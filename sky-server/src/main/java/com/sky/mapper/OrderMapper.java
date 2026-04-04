package com.sky.mapper;

import com.sky.entity.Orders;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {

    //插入订单数据
    @Insert("insert into orders (number, user_id, address_book_id, order_time, " +
            "checkout_time, amount, remark, phone, address, user_name, consignee, " +
            "cancel_reason, rejection_reason, cancel_time, estimated_delivery_time," +
            " delivery_time, pack_amount, tableware_number) VALUES " +
            "(#{number},#{userId},#{addressBookId},#{orderTime},#{checkoutTime}," +
            "#{amount},#{remark},#{phone},#{address},#{userName},#{consignee}," +
            "#{cancelReason},#{rejectionReason},#{cancelTime},#{estimatedDeliveryTime}," +
            "#{deliveryTime},#{packAmount},#{tablewareNumber})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void submit(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, Long id);


    //通过id查找订单信息
    @Select("SELECT * from orders where id=#{id}")
    Orders getById(Long id);
}
