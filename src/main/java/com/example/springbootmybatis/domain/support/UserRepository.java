package com.example.springbootmybatis.domain.support;

import com.example.springbootmybatis.domain.model.entity.User;
import java.util.List;

/**
 * 用户仓储接口（定义在Domain层）
 */
public interface UserRepository {
    
    /**
     * 根据ID查询用户
     */
    User findById(Long id);
    
    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);
    
    /**
     * 根据状态查询用户列表
     */
    List<User> findByStatus(Integer status);
    
    /**
     * 查询所有用户
     */
    List<User> findAll();
    
    /**
     * 保存用户（新增）
     */
    void save(User user);
    
    /**
     * 更新用户
     */
    void update(User user);
    
    /**
     * 删除用户
     */
    void delete(Long id);
}
