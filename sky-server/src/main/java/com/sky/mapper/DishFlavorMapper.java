package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    //新增口味
    void save(List<DishFlavor> dishFlavor);

    //批量删除口味
    void deleteBatch(List<Long> ids);

    //根据菜品id查找口味
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> queryById(long id);

}
