package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    //检测待支付超时订单
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> processTimeoutOrder(Integer status, LocalDateTime orderTime);

    //通过时间点获取时间段内营业额
    @Select("select sum(amount) from orders where order_time between #{beginTime} and #{endTime} and status=5")
    Double getTurnoverByDate(LocalDateTime beginTime, LocalDateTime endTime);

    //根据日期和订单状态查询订单数量
    Integer getCountByTimeAndStatus(Map<String, Object> map);

    //根据日期查询销量top10
    @Select("select od.name as name,sum(od.number) as number from order_detail od,orders o " +
            "where od.order_id = o.id and o.status = 5 " +
            "group by od.name " +
            "order by number desc " +
            "limit 0,10")
    List<GoodsSalesDTO> getTop10(LocalDateTime beginTime, LocalDateTime endTime);
}
