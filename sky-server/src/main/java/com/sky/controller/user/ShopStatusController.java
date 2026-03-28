package com.sky.controller.user;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("UserShopStatusController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopStatusController {

    private static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    //获取商铺营业状态
    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("设置商铺营业状态:{}", status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }

}
