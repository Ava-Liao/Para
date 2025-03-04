package com.graduate.para.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduate.para.model.User;
import com.graduate.para.service.UserService;
import com.graduate.para.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 650ji
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-03-04 14:23:50
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public boolean register(User user) {
        // 检查用户名和邮箱是否已存在
        if (checkUsername(user.getUsername()) || checkEmail(user.getEmail())) {
            return false;
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 保存用户
        return save(user);
    }

    @Override
    public User login(String username, String password) {
        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = getOne(queryWrapper);

        // 验证密码
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 登录成功，清除密码
            user.setPassword(null);
            return user;
        }
        return null;
    }

    @Override
    public boolean checkUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean checkEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return count(queryWrapper) > 0;
    }
}




