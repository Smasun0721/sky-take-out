package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    //查询菜品
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("条件分页查询菜品:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    //新增菜品
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }

    //设置起售、停售状态
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, long id) {
        log.info("设置起售、停售状态:{},{}", status, id);
        dishService.startOrStop(status, id);
        return Result.success();
    }

    //删除菜品
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("删除菜品:{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    //根据ID查找菜品
    @GetMapping("/{id}")
    public Result<DishVO> queryById(@PathVariable long id) {
        log.info("根据ID查找菜品:{}", id);
        DishVO dishvo = dishService.queryById(id);
        return Result.success(dishvo);
    }

    //根据分类id查找菜品
    @GetMapping("/list")
    public Result<List<DishVO>> queryByCategoryId(long categoryId) {
        log.info("根据ID查找菜品:{}", categoryId);
        List<DishVO> dishVOS = dishService.queryByCategoryId(categoryId);
        return Result.success(dishVOS);
    }

    //修改菜品
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

}
