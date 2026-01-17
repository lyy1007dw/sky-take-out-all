package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

/**
 * @author can dong
 */
public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO 微信登录参数
     * @return 登录结果
     */
    UserLoginVO wxLogin(UserLoginDTO userLoginDTO);
}
