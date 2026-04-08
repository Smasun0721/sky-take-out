package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    //条件分页查询菜品
    Page<DishVO> query(DishPageQueryDTO dishPageQueryDTO);

    //新增菜品
    @Insert("insert into dish (name, category_id, price, image, description, status, create_time, update_time, create_user, update_user) " +
            "values (#{name},#{categoryId},#{price},#{image},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(value = OperationType.INSERT)
    void save(Dish dish);

    //起售、停售菜品
    @Update("update dish set status=#{status},update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
    @AutoFill(value = OperationType.UPDATE)
    void startOrStop(Dish dish);

    //通过id查找菜品
    @Select("select * from dish where id=#{id}")
    Dish queryById(long id);

    //批量删除菜品
    void deleteBatch(List<Long> ids);

    //修改菜品
    @Update("update dish set name=#{name}, category_id=#{categoryId}, price=#{price}, image=#{image}, " +
            "description=#{description}, status=#{status},update_time=#{updateTime}, update_user=#{updateUser} " +
            "where id=#{id}")
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    //根据分类id查找菜品
    @Select("select * from dish where category_id=#{categortId}")
    List<DishVO> countByCategoryIdVO(long categoryId);

    /**
     * 动态条件查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
