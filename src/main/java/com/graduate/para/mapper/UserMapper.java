package com.graduate.para.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduate.para.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 650ji
* @description 针对表【user】的数据库操作Mapper
* @createDate 2025-03-04 14:23:50
* @Entity com.graduate.para.model.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




