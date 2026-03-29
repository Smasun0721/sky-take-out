package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    //条件查询菜品
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    //新增菜品
    void save(DishDTO dishDTO);

    //设置起售、停售状态
    void startOrStop(Integer status, long id);

    //删除菜品
    void deleteBatch(List<Long> ids);

    DishVO queryById(long id);

    void update(DishDTO dishDTO);
    //根据分类id查找菜品
    List<DishVO> queryByCategoryId(long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
