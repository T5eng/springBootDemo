package com.t5eng.miaosha.dao;

import com.t5eng.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Select("select * from user where id= #{id}") //相当于sql语句，{}引用 @Param的参数
    public User getById(@Param("id") int id);

    @Insert("insert into user(id, name) values( #{id}, #{name} )")
    public int insert(User user);
}
