package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    //通过菜品id查找套餐
    List<Long> queryByDishIds(List<Long> ids);

    //保存套餐菜品
    void save(List<SetmealDish> setmealDishes);

    //根据套餐id删除对应套餐下的所有菜品
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteAll(Long id);

    //通过套餐id查找菜品
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> queryById(long id);

    //根据套餐id批量删除对应套餐下的所有菜品
    void deleteAllList(List<Long> ids);
}
