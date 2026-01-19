package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author can dong
 */
@Service
public class UserServiceImpl implements UserService {
    /**
     * 微信服务请求地址
     */
    private static final String WIN_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;



    /**
     * 微信登录
     * @param userLoginDTO 微信登录参数
     * @return 登录结果
     */
    @Override
    public UserLoginVO wxLogin(UserLoginDTO userLoginDTO) {
        // 1.获取微信登录的openid
        String openid = getOpenid(userLoginDTO.getCode());

        // 2.判断openid是否有效
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 3.判断用户是否存在
        User user = userMapper.getByOpenId(openid);
        // 新用户自动注册
        User newUser = new User();
        if(user == null){
            newUser.setOpenid(openid);
            newUser.setCreateTime(LocalDateTime.now());
            userMapper.insert(newUser);
        }

        // 4.为用户生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, newUser.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        // 5.封装结果返回
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setId(newUser.getId());
        userLoginVO.setOpenid(openid);
        userLoginVO.setToken(token);
        return userLoginVO;
    }

    private String getOpenid(String code){
        // 封装请求内容
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        // 发送请求
        String json = HttpClientUtil.doGet(WIN_LOGIN, map);
        // 解析返回的结果
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
