package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    //新增员工
    @Insert("insert into employee (name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user) " +
            "values (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void save(Employee employee);

    //条件分页查询员工
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    //启用或禁用员工
    void startOrStop(Employee employee);

    //根据id查询员工
    @Select("select * from employee where id = #{id}")
    Employee queryById(Integer id);

    //编辑员工
    @Insert("update employee set name=#{name}, username=#{username},  phone=#{phone}, sex=#{sex}, id_number=#{idNumber}, update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
    void update(Employee employee);
}
