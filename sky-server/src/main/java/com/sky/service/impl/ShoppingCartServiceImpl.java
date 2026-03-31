package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;


    //添加购物车
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //判断当前商品在购物车中是否已存在
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.query(shoppingCart);

        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {    //存在则数量加一
            ShoppingCart cart = shoppingCartList.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumber(cart);
        } else {      //不存在则新增数据
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {   //当前添加的是菜品
                Dish dish = dishMapper.queryById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {      //当前添加的是套餐
                SetmealVO setmealVO = setmealMapper.queryById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmealVO.getName());
                shoppingCart.setImage(setmealVO.getImage());
                shoppingCart.setAmount(setmealVO.getPrice());
            }

            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.add(shoppingCart);
        }

    }

    //查看购物车
    @Override
    public List<ShoppingCart> list() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(userId)
                .build();
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.query(shoppingCart);
        return shoppingCartList;
    }
}
