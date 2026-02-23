package com.example.springbootmybatis.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}