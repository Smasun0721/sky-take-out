package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    //条件分页查询套餐
    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    //新增套餐
    void save(SetmealDTO setmealDTO);

    //根据id查询套餐
    SetmealVO queryById(long id);

    //修改套餐
    void update(SetmealDTO setmealDTO);

    //批量删除套餐
    void deleteBatch(List<Long> ids);

    //设置起售、停售状态
    void startOrStop(Integer status, long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

}
