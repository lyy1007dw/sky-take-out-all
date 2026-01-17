package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author can dong
 */
@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     * @param openid 微信登录成功返回的唯一标识id
     * @return 用户对象
     */
    @Select("select * from user where openid=#{openid}")
    public User getByOpenId(String openid);

    /**
     * 插入用户数据
     * @param newUser 新用户数据
     */
    void insert(User newUser);
}
