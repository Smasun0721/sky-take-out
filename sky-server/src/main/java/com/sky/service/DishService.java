package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {

    //条件查询菜品
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    //新增菜品
    void save(DishDTO dishDTO);
}
