package com.example.springbootmybatis.infrastructure.repository.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户持久化对象（与数据库表结构对应）
 */
@Data
public class UserPO {
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
