package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

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

    /**
     * 根据id查询用户
     * @param userId 用户id
     * @return 用户对象
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * 根据条件统计用户数量
     * @param map 查询条件
     * @return 用户数量
     */
    Integer countByMap(Map<String, Object> map);
}
