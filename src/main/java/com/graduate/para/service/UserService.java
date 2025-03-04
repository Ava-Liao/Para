package com.graduate.para.service;

import com.graduate.para.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 650ji
* @description 针对表【user】的数据库操作Service
* @createDate 2025-03-04 14:23:50
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    boolean register(User user);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户信息，失败返回null
     */
    User login(String username, String password);

    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 存在返回true，不存在返回false
     */
    boolean checkUsername(String username);

    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 存在返回true，不存在返回false
     */
    boolean checkEmail(String email);
}
