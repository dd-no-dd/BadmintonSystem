package com.wuli.badminton.dao;

import com.wuli.badminton.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);
    void insert(User user);
} 