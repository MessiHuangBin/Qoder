package com.example.springbootmybatis.application.service;

import com.example.springbootmybatis.application.convert.UserConvert;
import com.example.springbootmybatis.application.dto.UserDTO;
import com.example.springbootmybatis.domain.model.entity.User;
import com.example.springbootmybatis.domain.support.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户应用服务
 */
@Service
public class UserAppService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConvert userConvert;

    /**
     * 根据ID查询用户
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id);
        return userConvert.toDTO(user);
    }

    /**
     * 根据用户名查询用户
     */
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userConvert.toDTO(user);
    }

    /**
     * 根据状态查询用户列表
     */
    public List<UserDTO> getUsersByStatus(Integer status) {
        List<User> users = userRepository.findByStatus(status);
        return userConvert.toDTOList(users);
    }

    /**
     * 查询所有用户
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userConvert.toDTOList(users);
    }

    /**
     * 新增用户
     */
    public UserDTO addUser(UserDTO userDTO) {
        User user = userConvert.toDomain(userDTO);
        userRepository.save(user);
        return userConvert.toDTO(user);
    }

    /**
     * 更新用户
     */
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userConvert.toDomainForUpdate(userDTO);
        userRepository.update(user);
        // 查询更新后的用户返回
        User updatedUser = userRepository.findById(userDTO.getId());
        return userConvert.toDTO(updatedUser);
    }

    /**
     * 删除用户
     */
    public void deleteUser(Long id) {
        userRepository.delete(id);
    }
}
