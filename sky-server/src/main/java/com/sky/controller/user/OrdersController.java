package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrdersController")
@RequestMapping("/user/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    //历史订单查询
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("历史订单查询:{},当前用户:{}", ordersPageQueryDTO, BaseContext.getCurrentId());
        PageResult pageResult = ordersService.historyOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    //查询订单详情
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id){
        log.info("查询订单详情:{}",id);
        OrderVO orderVO = ordersService.queryDetailById(id);
        return Result.success(orderVO);
    }

    //取消订单
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id){
        log.info("取消订单:{}",id);
        ordersService.cancelById(id);
        return Result.success();
    }

    //再来一单
    @PostMapping("/repetition/{id}")
    public Result again(@PathVariable Long id){
        log.info("再来一单:{}",id);
        ordersService.again(id);
        return Result.success();
    }
}
