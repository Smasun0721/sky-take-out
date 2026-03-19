package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    //新增员工
    void save(EmployeeDTO employeeDTO);

    //分页条件查询员工
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    //启用或禁用员工
    void starOrStop(Integer status, long id);

    //根据id查询员工
    Employee queryById(Integer id);

    //编辑员工
    void update(EmployeeDTO employeeDTO);
}
