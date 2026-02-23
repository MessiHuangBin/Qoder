package com.example.springbootmybatis.mapper;

import com.example.springbootmybatis.entity.User;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {
    
    User selectById(Long id);
    
    User selectByUsername(String username);
    
    List<User> selectByStatus(Integer status);
    
    int insert(User user);
    
    int update(User user);
    
    int deleteById(Long id);
    
    List<User> selectAll();
}