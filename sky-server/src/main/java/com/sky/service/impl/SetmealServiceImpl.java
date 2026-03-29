package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageSerializable;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //条件分页查询套餐
    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page= setmealMapper.page(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //新增套餐
    @Override
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);    //添加套餐信息
        for(SetmealDish setmealDish:setmealDTO.getSetmealDishes()){
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.save(setmealDTO.getSetmealDishes());  //添加套餐菜品信息
    }


    //根据id查询套餐
    @Override
    public SetmealVO queryById(long id) {
        SetmealVO setmealVO= setmealMapper.queryById(id);
        List<SetmealDish> setmealDishes= setmealDishMapper.queryById(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    //修改套餐
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);     //修改套餐信息
        //修改套餐菜品信息
        for(SetmealDish setmealDish:setmealDTO.getSetmealDishes()){
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.deleteAll(setmealDTO.getId());    //删除套餐菜品信息
        setmealDishMapper.save(setmealDTO.getSetmealDishes());    //添加套餐菜品信息
    }

    //批量删除套餐
    @Override
    public void deleteBatch(List<Long> ids) {
        //删除对应套餐下的菜品
        setmealDishMapper.deleteAllList(ids);
        //根据id批量删除套餐
        setmealMapper.deleteBatch(ids);
    }

    //设置起售、停售状态
    @Override
    public void startOrStop(Integer status, long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.startOrStop(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
