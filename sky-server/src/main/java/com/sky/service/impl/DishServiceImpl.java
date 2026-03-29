package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //条件分页查询菜品
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.query(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //新增菜品
    @Override
    public void save(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.save(dish);

        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        if (dishDTO.getFlavors() != null) {
            dishFlavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dish.getId());
            });
            dishFlavorMapper.save(dishFlavors);
        }
    }

    //设置起售、停售状态
    @Override
    public void startOrStop(Integer status, long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.startOrStop(dish);
    }

    //删除菜品
    @Override
    public void deleteBatch(List<Long> ids) {

        //1.检查菜品是否为起售状态
        for (long id : ids) {
            Dish dish = dishMapper.queryById(id);
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //2.检查菜品是否被包含在套餐中
        List<Long> list = setmealDishMapper.queryByDishIds(ids);
        if (!list.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3.删除菜品
        dishMapper.deleteBatch(ids);

        //4.删除菜品对应的口味
        dishFlavorMapper.deleteBatch(ids);
    }

    //根据ID查找菜品
    @Override
    public DishVO queryById(long id) {

        DishVO dishVO = new DishVO();
        Dish dish = dishMapper.queryById(id);
        BeanUtils.copyProperties(dish, dishVO);

        //根据菜品id查找口味
        List<DishFlavor> flavorList = dishFlavorMapper.queryById(id);
        dishVO.setFlavors(flavorList);

        return dishVO;
    }

    //修改菜品
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        //更新菜品口味(先删除，后插入)
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(dish.getId());
        dishFlavorMapper.deleteBatch(ids);  //删除
        //为口味赋值菜品id
        List<DishFlavor> flavors = dishDTO.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            dishFlavor.setDishId(dish.getId());
        }
        dishFlavorMapper.save(flavors);


    }

    //根据分类id查找菜品
    @Override
    public List<DishVO> queryByCategoryId(long categoryId) {
        List<DishVO> list = dishMapper.countByCategoryIdVO(categoryId);
        return list;
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.queryById(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
