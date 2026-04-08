package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {

    //根据openid查找用户
    @Select("select * from user where openid=#{openid}")
    User queryByOpenid(String openid);

    //自动注册新用户
    @Insert("insert into user (openid, name, phone, sex, id_number, avatar, create_time) values " +
            "(#{openid},#{name},#{phone},#{sex},#{idNumber},#{avatar},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    //根据日期获取当前用户数量
    @Select(("select count(*) from user where create_time < #{time}"))
    Integer getUserCountByTime(LocalDateTime time);
}
