package com.example.springbootmybatis.infrastructure.repository.impl;

import com.example.springbootmybatis.domain.model.entity.User;
import com.example.springbootmybatis.domain.support.UserRepository;
import com.example.springbootmybatis.infrastructure.repository.convert.UserPOConvert;
import com.example.springbootmybatis.infrastructure.repository.mapper.UserMapper;
import com.example.springbootmybatis.infrastructure.repository.po.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户仓储实现类
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserPOConvert userPOConvert;

    @Override
    public User findById(Long id) {
        UserPO po = userMapper.selectById(id);
        return userPOConvert.toDomain(po);
    }

    @Override
    public User findByUsername(String username) {
        UserPO po = userMapper.selectByUsername(username);
        return userPOConvert.toDomain(po);
    }

    @Override
    public List<User> findByStatus(Integer status) {
        List<UserPO> poList = userMapper.selectByStatus(status);
        return userPOConvert.toDomainList(poList);
    }

    @Override
    public List<User> findAll() {
        List<UserPO> poList = userMapper.selectAll();
        return userPOConvert.toDomainList(poList);
    }

    @Override
    public void save(User user) {
        UserPO po = userPOConvert.toPO(user);
        userMapper.insert(po);
        // 将生成的ID设置回user
        user.setId(po.getId());
    }

    @Override
    public void update(User user) {
        UserPO po = userPOConvert.toPO(user);
        userMapper.update(po);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }
}
