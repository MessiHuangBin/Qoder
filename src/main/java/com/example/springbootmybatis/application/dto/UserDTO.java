package com.example.springbootmybatis.application.dto;

/**
 * 用户X包（上游传入的业务DTO）
 */
public class UserDTO {
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getMaxBorrowCount() {
        return maxBorrowCount;
    }

    public void setMaxBorrowCount(Integer maxBorrowCount) {
        this.maxBorrowCount = maxBorrowCount;
    }

    public Integer getCurrentBorrowCount() {
        return currentBorrowCount;
    }

    public void setCurrentBorrowCount(Integer currentBorrowCount) {
        this.currentBorrowCount = currentBorrowCount;
    }
}
