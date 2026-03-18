package com.example.springbootmybatis.adapter.controller;

import com.example.springbootmybatis.application.dto.UserDTO;
import com.example.springbootmybatis.application.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器（适配器层）
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserAppService userAppService;

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        UserDTO user = userAppService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("用户不存在，ID: " + id);
        }



        
        return user;
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        UserDTO user = userAppService.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在，用户名: " + username);
        }
        return user;
    }

    @GetMapping("/status/{status}")
    public List<UserDTO> getUsersByStatus(@PathVariable Integer status) {
        return userAppService.getUsersByStatus(status);
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userAppService.getAllUsers();
    }

    @GetMapping("/test-error")
    public UserDTO testError() {
        throw new RuntimeException("测试错误");
    }

    @PostMapping
    public UserDTO addUser(@RequestBody UserDTO userDTO) {
        return userAppService.addUser(userDTO);
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        return userAppService.updateUser(userDTO);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        userAppService.deleteUser(id);
        return true;
    }
}
