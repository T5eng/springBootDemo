package com.t5eng.miaosha.dao;

import com.t5eng.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaUserDao {
    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(     @Param("id")     long id); //根据id从Redis从获取
}
