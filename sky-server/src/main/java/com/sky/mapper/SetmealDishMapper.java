package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //通过菜品id查找套餐
    List<Long> queryByDishIds(List<Long> ids);
}
