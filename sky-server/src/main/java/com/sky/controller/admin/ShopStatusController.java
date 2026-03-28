package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("AdminShopStatusController")
@RequestMapping("/admin/shop")
@Slf4j
public class ShopStatusController {

    private static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    //设置商铺营业状态
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status) {
        log.info("设置商铺营业状态:{}", status == 1 ? "营业中" : "打样中");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    //获取商铺营业状态
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("设置商铺营业状态:{}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }


}
