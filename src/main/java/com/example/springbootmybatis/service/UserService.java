package com.example.springbootmybatis.service;

import com.example.springbootmybatis.entity.User;
import com.example.springbootmybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }
    
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    public List<User> getUsersByStatus(Integer status) {
        return userMapper.selectByStatus(status);
    }
    
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }
    
    public int addUser(User user) {
        return userMapper.insert(user);
    }
    
    public int updateUser(User user) {
        return userMapper.update(user);
    }
    
    public int deleteUser(Long id) {
        return userMapper.deleteById(id);
    }
}