package com.example.springbootmybatis.domain.model.entity;

import java.time.LocalDateTime;

/**
 * 用户领域实体
 */
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private Integer userType;
    private Integer status;
    private Integer maxBorrowCount;
    private Integer currentBorrowCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;

    // 领域行为方法
    public static User create(String username, String password, String realName, String email, String phone) {
        User user = new User();
        user.username = username;
        user.password = password;
        user.realName = realName;
        user.email = email;
        user.phone = phone;
        user.status = 1;
        user.maxBorrowCount = 10;
        user.currentBorrowCount = 0;
        user.deleted = 0;
        user.createTime = LocalDateTime.now();
        user.updateTime = LocalDateTime.now();
        return user;
    }

    public void updateInfo(String realName, String email, String phone) {
        this.realName = realName;
        this.email = email;
        this.phone = phone;
        this.updateTime = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = 0;
        this.updateTime = LocalDateTime.now();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getUserType() {
        return userType;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getMaxBorrowCount() {
        return maxBorrowCount;
    }

    public Integer getCurrentBorrowCount() {
        return currentBorrowCount;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    // Setters (用于从PO转换时设置值)
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setMaxBorrowCount(Integer maxBorrowCount) {
        this.maxBorrowCount = maxBorrowCount;
    }

    public void setCurrentBorrowCount(Integer currentBorrowCount) {
        this.currentBorrowCount = currentBorrowCount;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
}
