package com.example.springbootmybatis.infrastructure.repository.po;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 借阅记录持久化对象
 */
@Data
public class BorrowRecordPO {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Integer status;
    private Integer renewCount;
    private BigDecimal fineAmount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
