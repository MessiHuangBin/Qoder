# 图书借阅功能实现计划

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现图书借阅功能，支持借阅、归还和查询用户当前借阅列表。

**Architecture:** 按照项目现有 DDD 分层架构，新增 BorrowRecord 实体和相关分层代码，通过领域服务协调图书和用户的计数更新。

**Tech Stack:** Java, Spring Boot, MyBatis, MySQL

---

## 文件结构

### 新增文件

**Domain Layer:**
- `src/main/java/com/example/springbootmybatis/domain/model/entity/BorrowRecord.java` - 借阅记录领域实体
- `src/main/java/com/example/springbootmybatis/domain/service/BorrowDomainService.java` - 借阅领域服务
- `src/main/java/com/example/springbootmybatis/domain/support/BorrowRepository.java` - 借阅仓储接口

**Infrastructure Layer:**
- `src/main/java/com/example/springbootmybatis/infrastructure/repository/po/BorrowRecordPO.java` - 借阅记录持久化对象
- `src/main/java/com/example/springbootmybatis/infrastructure/repository/mapper/BorrowMapper.java` - MyBatis Mapper接口
- `src/main/java/com/example/springbootmybatis/infrastructure/repository/convert/BorrowPOConvert.java` - PO转换器
- `src/main/java/com/example/springbootmybatis/infrastructure/repository/impl/BorrowRepositoryImpl.java` - 仓储实现
- `src/main/resources/mapper/BorrowMapper.xml` - MyBatis XML映射文件

**Application Layer:**
- `src/main/java/com/example/springbootmybatis/application/dto/BorrowDTO.java` - 借阅DTO
- `src/main/java/com/example/springbootmybatis/application/convert/BorrowConvert.java` - DTO转换器
- `src/main/java/com/example/springbootmybatis/application/service/BorrowAppService.java` - 借阅应用服务

**Adapter Layer:**
- `src/main/java/com/example/springbootmybatis/adapter/controller/BorrowController.java` - 借阅控制器

**Test:**
- `src/test/java/com/example/springbootmybatis/repository/BorrowRepositoryTest.java` - 仓储层测试

---

## Chunk 1: 数据库表创建

### Task 1: 创建借阅记录数据库表

**Files:**
- Create: 数据库表 `borrow_record`

- [ ] **Step 1: 执行建表SQL**

在 MySQL 数据库中执行：

```sql
CREATE TABLE IF NOT EXISTS borrow_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '图书ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-借阅中，2-已归还',
    borrow_time DATETIME NOT NULL COMMENT '借阅时间',
    return_time DATETIME COMMENT '归还时间',
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    deleted TINYINT NOT NULL DEFAULT 0,
    INDEX idx_user_id (user_id),
    INDEX idx_book_id (book_id),
    INDEX idx_status (status)
) COMMENT='借阅记录表';
```

- [ ] **Step 2: 验证表创建成功**

```sql
SHOW TABLES LIKE 'borrow_record';
DESC borrow_record;
```

Expected: 表结构正确创建

---

## Chunk 2: 领域层实现

### Task 2: 创建 BorrowRecord 领域实体

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/domain/model/entity/BorrowRecord.java`

- [ ] **Step 1: 创建领域实体文件**

```java
package com.example.springbootmybatis.domain.model.entity;

import java.time.LocalDateTime;

/**
 * 借阅记录领域实体
 */
public class BorrowRecord {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer status; // 1-借阅中，2-已归还
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;

    // 工厂方法：创建借阅记录
    public static BorrowRecord create(Long userId, Long bookId) {
        BorrowRecord record = new BorrowRecord();
        record.userId = userId;
        record.bookId = bookId;
        record.status = 1;
        record.borrowTime = LocalDateTime.now();
        record.deleted = 0;
        record.createTime = LocalDateTime.now();
        record.updateTime = LocalDateTime.now();
        return record;
    }

    // 领域行为：归还图书
    public void returnBook() {
        this.status = 2;
        this.returnTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public Integer getStatus() { return status; }
    public LocalDateTime getBorrowTime() { return borrowTime; }
    public LocalDateTime getReturnTime() { return returnTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public Integer getDeleted() { return deleted; }

    // Setters (用于从PO转换)
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setStatus(Integer status) { this.status = status; }
    public void setBorrowTime(LocalDateTime borrowTime) { this.borrowTime = borrowTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
```

- [ ] **Step 2: 编译验证**

Run: `mvn compile -q`
Expected: BUILD SUCCESS

### Task 3: 创建 BorrowRepository 仓储接口

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/domain/support/BorrowRepository.java`

- [ ] **Step 1: 创建仓储接口文件**

```java
package com.example.springbootmybatis.domain.support;

import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import java.util.List;

/**
 * 借阅记录仓储接口
 */
public interface BorrowRepository {
    
    /**
     * 保存借阅记录
     */
    void save(BorrowRecord record);
    
    /**
     * 根据ID查询借阅记录
     */
    BorrowRecord findById(Long id);
    
    /**
     * 更新借阅记录
     */
    void update(BorrowRecord record);
    
    /**
     * 查询用户的当前借阅列表（状态为借阅中）
     */
    List<BorrowRecord> findByUserIdAndStatus(Long userId, Integer status);
    
    /**
     * 查询图书的当前借阅列表（状态为借阅中）
     */
    List<BorrowRecord> findByBookIdAndStatus(Long bookId, Integer status);
}
```

### Task 4: 创建 BorrowDomainService 领域服务

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/domain/service/BorrowDomainService.java`

- [ ] **Step 1: 创建领域服务文件**

```java
package com.example.springbootmybatis.domain.service;

import com.example.springbootmybatis.domain.model.entity.Book;
import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import com.example.springbootmybatis.domain.model.entity.User;
import com.example.springbootmybatis.domain.support.BorrowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 借阅领域服务
 */
@Service
public class BorrowDomainService {
    
    private final BorrowRepository borrowRepository;
    
    public BorrowDomainService(BorrowRepository borrowRepository) {
        this.borrowRepository = borrowRepository;
    }
    
    /**
     * 执行借阅操作
     * 前置条件：图书和用户已通过校验
     */
    @Transactional
    public BorrowRecord borrowBook(Long userId, Long bookId) {
        // 创建借阅记录
        BorrowRecord record = BorrowRecord.create(userId, bookId);
        borrowRepository.save(record);
        return record;
    }
    
    /**
     * 执行归还操作
     */
    @Transactional
    public BorrowRecord returnBook(Long borrowRecordId) {
        BorrowRecord record = borrowRepository.findById(borrowRecordId);
        if (record == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        if (record.getStatus() != 1) {
            throw new RuntimeException("该记录已归还");
        }
        
        record.returnBook();
        borrowRepository.update(record);
        return record;
    }
}
```

---

## Chunk 3: 基础设施层实现

### Task 5: 创建 BorrowRecordPO 持久化对象

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/infrastructure/repository/po/BorrowRecordPO.java`

- [ ] **Step 1: 创建PO文件**

```java
package com.example.springbootmybatis.infrastructure.repository.po;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 借阅记录持久化对象
 */
@Data
public class BorrowRecordPO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer status;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
```

### Task 6: 创建 BorrowMapper 接口和 XML

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/infrastructure/repository/mapper/BorrowMapper.java`
- Create: `src/main/resources/mapper/BorrowMapper.xml`

- [ ] **Step 1: 创建 Mapper 接口**

```java
package com.example.springbootmybatis.infrastructure.repository.mapper;

import com.example.springbootmybatis.infrastructure.repository.po.BorrowRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 借阅记录Mapper
 */
@Mapper
public interface BorrowMapper {
    
    int insert(BorrowRecordPO po);
    
    BorrowRecordPO selectById(@Param("id") Long id);
    
    int update(BorrowRecordPO po);
    
    List<BorrowRecordPO> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    List<BorrowRecordPO> selectByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") Integer status);
}
```

- [ ] **Step 2: 创建 Mapper XML**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.springbootmybatis.infrastructure.repository.mapper.BorrowMapper">

    <resultMap id="BaseResultMap" type="com.example.springbootmybatis.infrastructure.repository.po.BorrowRecordPO">
        <id column="id" property="id"/>
        <result column="user_id" property="userId"/>
        <result column="book_id" property="bookId"/>
        <result column="status" property="status"/>
        <result column="borrow_time" property="borrowTime"/>
        <result column="return_time" property="returnTime"/>
        <result column="create_time" property="createTime"/>
        <result column="update_time" property="updateTime"/>
        <result column="deleted" property="deleted"/>
    </resultMap>

    <insert id="insert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO borrow_record (user_id, book_id, status, borrow_time, return_time, create_time, update_time, deleted)
        VALUES (#{userId}, #{bookId}, #{status}, #{borrowTime}, #{returnTime}, #{createTime}, #{updateTime}, #{deleted})
    </insert>

    <select id="selectById" resultMap="BaseResultMap">
        SELECT * FROM borrow_record WHERE id = #{id} AND deleted = 0
    </select>

    <update id="update">
        UPDATE borrow_record
        SET user_id = #{userId},
            book_id = #{bookId},
            status = #{status},
            borrow_time = #{borrowTime},
            return_time = #{returnTime},
            update_time = #{updateTime}
        WHERE id = #{id} AND deleted = 0
    </update>

    <select id="selectByUserIdAndStatus" resultMap="BaseResultMap">
        SELECT * FROM borrow_record 
        WHERE user_id = #{userId} AND status = #{status} AND deleted = 0
        ORDER BY borrow_time DESC
    </select>

    <select id="selectByBookIdAndStatus" resultMap="BaseResultMap">
        SELECT * FROM borrow_record 
        WHERE book_id = #{bookId} AND status = #{status} AND deleted = 0
        ORDER BY borrow_time DESC
    </select>

</mapper>
```

### Task 7: 创建 BorrowPOConvert 转换器

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/infrastructure/repository/convert/BorrowPOConvert.java`

- [ ] **Step 1: 创建转换器文件**

```java
package com.example.springbootmybatis.infrastructure.repository.convert;

import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import com.example.springbootmybatis.infrastructure.repository.po.BorrowRecordPO;

/**
 * BorrowRecord 与 PO 之间的转换器
 */
public class BorrowPOConvert {
    
    public static BorrowRecordPO toPO(BorrowRecord record) {
        if (record == null) {
            return null;
        }
        BorrowRecordPO po = new BorrowRecordPO();
        po.setId(record.getId());
        po.setUserId(record.getUserId());
        po.setBookId(record.getBookId());
        po.setStatus(record.getStatus());
        po.setBorrowTime(record.getBorrowTime());
        po.setReturnTime(record.getReturnTime());
        po.setCreateTime(record.getCreateTime());
        po.setUpdateTime(record.getUpdateTime());
        po.setDeleted(record.getDeleted());
        return po;
    }
    
    public static BorrowRecord toEntity(BorrowRecordPO po) {
        if (po == null) {
            return null;
        }
        BorrowRecord record = new BorrowRecord();
        record.setId(po.getId());
        record.setUserId(po.getUserId());
        record.setBookId(po.getBookId());
        record.setStatus(po.getStatus());
        record.setBorrowTime(po.getBorrowTime());
        record.setReturnTime(po.getReturnTime());
        record.setCreateTime(po.getCreateTime());
        record.setUpdateTime(po.getUpdateTime());
        record.setDeleted(po.getDeleted());
        return record;
    }
}
```

### Task 8: 创建 BorrowRepositoryImpl 仓储实现

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/infrastructure/repository/impl/BorrowRepositoryImpl.java`

- [ ] **Step 1: 创建仓储实现文件**

```java
package com.example.springbootmybatis.infrastructure.repository.impl;

import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import com.example.springbootmybatis.domain.support.BorrowRepository;
import com.example.springbootmybatis.infrastructure.repository.convert.BorrowPOConvert;
import com.example.springbootmybatis.infrastructure.repository.mapper.BorrowMapper;
import com.example.springbootmybatis.infrastructure.repository.po.BorrowRecordPO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 借阅记录仓储实现
 */
@Repository
public class BorrowRepositoryImpl implements BorrowRepository {
    
    private final BorrowMapper borrowMapper;
    
    public BorrowRepositoryImpl(BorrowMapper borrowMapper) {
        this.borrowMapper = borrowMapper;
    }
    
    @Override
    public void save(BorrowRecord record) {
        BorrowRecordPO po = BorrowPOConvert.toPO(record);
        borrowMapper.insert(po);
        record.setId(po.getId());
    }
    
    @Override
    public BorrowRecord findById(Long id) {
        BorrowRecordPO po = borrowMapper.selectById(id);
        return BorrowPOConvert.toEntity(po);
    }
    
    @Override
    public void update(BorrowRecord record) {
        BorrowRecordPO po = BorrowPOConvert.toPO(record);
        borrowMapper.update(po);
    }
    
    @Override
    public List<BorrowRecord> findByUserIdAndStatus(Long userId, Integer status) {
        List<BorrowRecordPO> poList = borrowMapper.selectByUserIdAndStatus(userId, status);
        return poList.stream()
                .map(BorrowPOConvert::toEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BorrowRecord> findByBookIdAndStatus(Long bookId, Integer status) {
        List<BorrowRecordPO> poList = borrowMapper.selectByBookIdAndStatus(bookId, status);
        return poList.stream()
                .map(BorrowPOConvert::toEntity)
                .collect(Collectors.toList());
    }
}
```

---

## Chunk 4: 应用层实现

### Task 9: 创建 BorrowDTO

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/application/dto/BorrowDTO.java`

- [ ] **Step 1: 创建 DTO 文件**

```java
package com.example.springbootmybatis.application.dto;

import java.time.LocalDateTime;

/**
 * 借阅记录DTO
 */
public class BorrowDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private Integer status;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    
    public LocalDateTime getBorrowTime() { return borrowTime; }
    public void setBorrowTime(LocalDateTime borrowTime) { this.borrowTime = borrowTime; }
    
    public LocalDateTime getReturnTime() { return returnTime; }
    public void setReturnTime(LocalDateTime returnTime) { this.returnTime = returnTime; }
}
```

### Task 10: 创建 BorrowConvert

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/application/convert/BorrowConvert.java`

- [ ] **Step 1: 创建转换器文件**

```java
package com.example.springbootmybatis.application.convert;

import com.example.springbootmybatis.application.dto.BorrowDTO;
import com.example.springbootmybatis.domain.model.entity.BorrowRecord;

/**
 * BorrowRecord 与 DTO 之间的转换器
 */
public class BorrowConvert {
    
    public static BorrowDTO toDTO(BorrowRecord record) {
        if (record == null) {
            return null;
        }
        BorrowDTO dto = new BorrowDTO();
        dto.setId(record.getId());
        dto.setUserId(record.getUserId());
        dto.setBookId(record.getBookId());
        dto.setStatus(record.getStatus());
        dto.setBorrowTime(record.getBorrowTime());
        dto.setReturnTime(record.getReturnTime());
        return dto;
    }
}
```

### Task 11: 创建 BorrowAppService

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/application/service/BorrowAppService.java`

- [ ] **Step 1: 创建应用服务文件**

```java
package com.example.springbootmybatis.application.service;

import com.example.springbootmybatis.application.convert.BorrowConvert;
import com.example.springbootmybatis.application.dto.BorrowDTO;
import com.example.springbootmybatis.domain.model.entity.Book;
import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import com.example.springbootmybatis.domain.model.entity.User;
import com.example.springbootmybatis.domain.service.BorrowDomainService;
import com.example.springbootmybatis.domain.support.BookRepository;
import com.example.springbootmybatis.domain.support.BorrowRepository;
import com.example.springbootmybatis.domain.support.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 借阅应用服务
 */
@Service
public class BorrowAppService {
    
    private final BorrowDomainService borrowDomainService;
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    
    public BorrowAppService(BorrowDomainService borrowDomainService,
                           BorrowRepository borrowRepository,
                           BookRepository bookRepository,
                           UserRepository userRepository) {
        this.borrowDomainService = borrowDomainService;
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
    
    /**
     * 借阅图书
     */
    @Transactional
    public BorrowDTO borrowBook(Long userId, Long bookId) {
        // 查询用户
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getStatus() != 1) {
            throw new RuntimeException("用户状态异常");
        }
        
        // 查询图书
        Book book = bookRepository.findById(bookId);
        if (book == null) {
            throw new RuntimeException("图书不存在");
        }
        if (book.getStatus() != 1) {
            throw new RuntimeException("图书不可借阅");
        }
        
        // 校验借阅条件
        if (book.getAvailableCount() <= 0) {
            throw new RuntimeException("图书已借完");
        }
        if (user.getCurrentBorrowCount() >= user.getMaxBorrowCount()) {
            throw new RuntimeException("用户借阅数量已达上限");
        }
        
        // 执行借阅
        BorrowRecord record = borrowDomainService.borrowBook(userId, bookId);
        
        // 更新图书可借数量
        book.borrow();
        bookRepository.update(book);
        
        // 更新用户借阅数量
        user.setCurrentBorrowCount(user.getCurrentBorrowCount() + 1);
        userRepository.update(user);
        
        return BorrowConvert.toDTO(record);
    }
    
    /**
     * 归还图书
     */
    @Transactional
    public BorrowDTO returnBook(Long borrowRecordId) {
        // 查询借阅记录
        BorrowRecord record = borrowRepository.findById(borrowRecordId);
        if (record == null) {
            throw new RuntimeException("借阅记录不存在");
        }
        
        // 执行归还
        borrowDomainService.returnBook(borrowRecordId);
        
        // 更新图书可借数量
        Book book = bookRepository.findById(record.getBookId());
        if (book != null) {
            book.returnBook();
            bookRepository.update(book);
        }
        
        // 更新用户借阅数量
        User user = userRepository.findById(record.getUserId());
        if (user != null && user.getCurrentBorrowCount() > 0) {
            user.setCurrentBorrowCount(user.getCurrentBorrowCount() - 1);
            userRepository.update(user);
        }
        
        return BorrowConvert.toDTO(record);
    }
    
    /**
     * 查询用户当前借阅列表
     */
    public List<BorrowDTO> getUserCurrentBorrows(Long userId) {
        List<BorrowRecord> records = borrowRepository.findByUserIdAndStatus(userId, 1);
        return records.stream()
                .map(BorrowConvert::toDTO)
                .collect(Collectors.toList());
    }
}
```

---

## Chunk 5: 适配器层实现

### Task 12: 创建 BorrowController

**Files:**
- Create: `src/main/java/com/example/springbootmybatis/adapter/controller/BorrowController.java`

- [ ] **Step 1: 创建控制器文件**

```java
package com.example.springbootmybatis.adapter.controller;

import com.example.springbootmybatis.application.dto.BorrowDTO;
import com.example.springbootmybatis.application.service.BorrowAppService;
import com.example.springbootmybatis.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 借阅控制器
 */
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {
    
    private final BorrowAppService borrowAppService;
    
    public BorrowController(BorrowAppService borrowAppService) {
        this.borrowAppService = borrowAppService;
    }
    
    /**
     * 借阅图书
     * POST /api/borrow
     * Body: {"userId": 1, "bookId": 1}
     */
    @PostMapping
    public Result<BorrowDTO> borrowBook(@RequestBody Map<String, Long> params) {
        Long userId = params.get("userId");
        Long bookId = params.get("bookId");
        
        if (userId == null || bookId == null) {
            return Result.error("用户ID和图书ID不能为空");
        }
        
        try {
            BorrowDTO dto = borrowAppService.borrowBook(userId, bookId);
            return Result.success(dto);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 归还图书
     * POST /api/borrow/{id}/return
     */
    @PostMapping("/{id}/return")
    public Result<BorrowDTO> returnBook(@PathVariable Long id) {
        try {
            BorrowDTO dto = borrowAppService.returnBook(id);
            return Result.success(dto);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    /**
     * 查询用户当前借阅列表
     * GET /api/borrow/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public Result<List<BorrowDTO>> getUserCurrentBorrows(@PathVariable Long userId) {
        List<BorrowDTO> list = borrowAppService.getUserCurrentBorrows(userId);
        return Result.success(list);
    }
}
```

---

## Chunk 6: 测试与验证

### Task 13: 创建 BorrowRepositoryTest

**Files:**
- Create: `src/test/java/com/example/springbootmybatis/repository/BorrowRepositoryTest.java`

- [ ] **Step 1: 创建测试文件**

```java
package com.example.springbootmybatis.repository;

import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import com.example.springbootmybatis.domain.support.BorrowRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BorrowRepositoryTest {
    
    @Autowired
    private BorrowRepository borrowRepository;
    
    @Test
    public void testSaveAndFind() {
        // 创建借阅记录
        BorrowRecord record = BorrowRecord.create(1L, 1L);
        
        // 保存
        borrowRepository.save(record);
        assertNotNull(record.getId());
        
        // 查询
        BorrowRecord found = borrowRepository.findById(record.getId());
        assertNotNull(found);
        assertEquals(1L, found.getUserId());
        assertEquals(1L, found.getBookId());
        assertEquals(1, found.getStatus());
    }
    
    @Test
    public void testUpdate() {
        // 创建并保存
        BorrowRecord record = BorrowRecord.create(1L, 1L);
        borrowRepository.save(record);
        
        // 更新
        record.returnBook();
        borrowRepository.update(record);
        
        // 验证
        BorrowRecord updated = borrowRepository.findById(record.getId());
        assertEquals(2, updated.getStatus());
        assertNotNull(updated.getReturnTime());
    }
    
    @Test
    public void testFindByUserIdAndStatus() {
        // 创建两条记录
        BorrowRecord record1 = BorrowRecord.create(1L, 1L);
        BorrowRecord record2 = BorrowRecord.create(1L, 2L);
        record2.returnBook(); // 已归还
        
        borrowRepository.save(record1);
        borrowRepository.save(record2);
        
        // 查询借阅中的记录
        List<BorrowRecord> list = borrowRepository.findByUserIdAndStatus(1L, 1);
        assertEquals(1, list.size());
        assertEquals(1L, list.get(0).getBookId());
    }
}
```

### Task 14: 运行测试验证

- [ ] **Step 1: 编译项目**

Run: `mvn clean compile -q`
Expected: BUILD SUCCESS

- [ ] **Step 2: 运行单元测试**

Run: `mvn test -Dtest=BorrowRepositoryTest -q`
Expected: Tests run: 3, Failures: 0, Errors: 0

- [ ] **Step 3: 启动应用验证**

Run: `mvn spring-boot:run`

测试接口：
1. 借阅图书：
```bash
curl -X POST http://localhost:8080/api/borrow \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "bookId": 1}'
```

2. 查询用户借阅：
```bash
curl http://localhost:8080/api/borrow/user/1
```

3. 归还图书：
```bash
curl -X POST http://localhost:8080/api/borrow/1/return
```

---

## 执行路径

**如果 harness 有 subagents (Claude Code 等):**
- **REQUIRED:** Use superpowers:subagent-driven-development
- 每个 Task 一个子代理
- 两阶段审查 (spec compliance + code quality)

**如果 harness 没有 subagents:**
- Execute plan in current session using superpowers:executing-plans
- 批量执行，检查点审查
