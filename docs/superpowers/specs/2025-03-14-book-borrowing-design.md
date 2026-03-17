# 图书借阅功能设计文档

> 创建时间：2025-03-14
> 功能范围：简单图书借阅记录

---

## 1. 功能概述

开发一个简单的图书借阅记录功能，支持：
- 用户借阅图书
- 用户归还图书
- 查询用户的当前借阅列表

## 2. 需求确认

### 2.1 功能范围
- **A. 简单借阅记录** - 只记录谁借了哪本书，不涉及归还日期、逾期等复杂逻辑

### 2.2 查询需求
- **A. 仅支持查询用户的当前借阅列表**（谁在借什么书，还没还）

### 2.3 业务规则
1. **借阅时检查：**
   - 图书可借数量 (`availableCount`) 必须 > 0
   - 用户当前借阅数量 (`currentBorrowCount`) 必须 < 最大借阅数量 (`maxBorrowCount`)

2. **数据一致性：**
   - 创建借阅记录
   - 减少图书可借数量
   - 增加用户当前借阅数量

3. **归还时：**
   - 更新借阅记录状态为"已归还"
   - 增加图书可借数量
   - 减少用户当前借阅数量

## 3. 领域模型设计

### 3.1 新增实体：BorrowRecord（借阅记录）

```java
package com.example.springbootmybatis.domain.model.entity;

import java.time.LocalDateTime;

/**
 * 借阅记录领域实体
 */
public class BorrowRecord {
    private Long id;
    private Long userId;           // 用户ID
    private Long bookId;           // 图书ID
    private Integer status;        // 状态：1-借阅中，2-已归还
    private LocalDateTime borrowTime;  // 借阅时间
    private LocalDateTime returnTime;  // 归还时间（可为空）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;

    // 领域行为方法
    public static BorrowRecord create(Long userId, Long bookId) {
        BorrowRecord record = new BorrowRecord();
        record.userId = userId;
        record.bookId = bookId;
        record.status = 1; // 借阅中
        record.borrowTime = LocalDateTime.now();
        record.deleted = 0;
        record.createTime = LocalDateTime.now();
        record.updateTime = LocalDateTime.now();
        return record;
    }

    public void returnBook() {
        this.status = 2; // 已归还
        this.returnTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
```

## 4. 架构设计

按照项目现有的 DDD 分层架构：

```
┌─────────────────────────────────────────────────────────────┐
│  adapter/controller/BorrowController.java    ← 接收HTTP请求  │
├─────────────────────────────────────────────────────────────┤
│  application/service/BorrowAppService.java   ← 应用服务编排  │
│  application/dto/BorrowDTO.java              ← 数据传输对象  │
│  application/convert/BorrowConvert.java      ← 对象转换      │
├─────────────────────────────────────────────────────────────┤
│  domain/model/entity/BorrowRecord.java       ← 领域实体      │
│  domain/service/BorrowDomainService.java     ← 领域服务      │
│  domain/support/BorrowRepository.java        ← 仓储接口      │
├─────────────────────────────────────────────────────────────┤
│  infrastructure/repository/po/BorrowRecordPO.java    ← 持久化对象 │
│  infrastructure/repository/mapper/BorrowMapper.java  ← MyBatis映射 │
│  infrastructure/repository/convert/BorrowPOConvert.java ← PO转换 │
│  infrastructure/repository/impl/BorrowRepositoryImpl.java ← 仓储实现 │
└─────────────────────────────────────────────────────────────┘
```

## 5. API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `POST /api/borrow` | 借阅图书 | 参数：userId, bookId |
| `POST /api/borrow/{id}/return` | 归还图书 | 参数：借阅记录ID |
| `GET /api/borrow/user/{userId}` | 查询用户当前借阅 | 返回：借阅中列表 |

## 6. 数据库表设计

```sql
CREATE TABLE borrow_record (
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

## 7. 核心业务流程

### 7.1 借阅流程
```
1. 接收借阅请求 (userId, bookId)
2. 查询用户和图书信息
3. 校验：图书可借数量 > 0
4. 校验：用户当前借阅 < 最大借阅数
5. 创建借阅记录
6. 更新图书可借数量 (availableCount--)
7. 更新用户当前借阅数 (currentBorrowCount++)
8. 返回借阅记录
```

### 7.2 归还流程
```
1. 接收归还请求 (borrowRecordId)
2. 查询借阅记录
3. 校验：记录存在且状态为"借阅中"
4. 更新借阅记录状态为"已归还"
5. 更新图书可借数量 (availableCount++)
6. 更新用户当前借阅数 (currentBorrowCount--)
7. 返回更新后的记录
```

## 8. 待办事项

- [ ] 创建数据库表
- [ ] 实现领域层（实体、服务、仓储接口）
- [ ] 实现基础设施层（PO、Mapper、仓储实现）
- [ ] 实现应用层（DTO、Convert、AppService）
- [ ] 实现适配器层（Controller）
- [ ] 编写单元测试
- [ ] 功能验证
