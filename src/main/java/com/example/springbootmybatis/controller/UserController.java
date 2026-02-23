package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.entity.User;
import com.example.springbootmybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }
    
    @GetMapping("/status/{status}")
    public List<User> getUsersByStatus(@PathVariable Integer status) {
        return userService.getUsersByStatus(status);
    }
    
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/test-error")
    public User testError() {
        throw new RuntimeException("测试错误");
    }
    
    @PostMapping
    public User addUser(@RequestBody User user) {
        int result = userService.addUser(user);
        if (result <= 0) {
            throw new RuntimeException("用户添加失败");
        }
        return user; // 返回新创建的用户对象
    }
    
    @PutMapping
    public User updateUser(@RequestBody User user) {
        int result = userService.updateUser(user);
        if (result <= 0) {
            throw new RuntimeException("用户更新失败");
        }
        return userService.getUserById(user.getId()); // 返回更新后的用户对象
    }
    
    @DeleteMapping("/{id}")
    public Boolean deleteUser(@PathVariable Long id) {
        int result = userService.deleteUser(id);
        if (result <= 0) {
            throw new RuntimeException("用户删除失败");
        }
        return true; // 删除成功返回true
    }
}