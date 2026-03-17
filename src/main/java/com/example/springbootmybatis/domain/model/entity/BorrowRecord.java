package com.example.springbootmybatis.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 借阅记录领域实体
 */
public class BorrowRecord {
    private Long id;
    private Long userId;
    private Long bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Integer status; // 1-借阅中，2-已归还
    private Integer renewCount;
    private BigDecimal fineAmount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 工厂方法：创建借阅记录
    public static BorrowRecord create(Long userId, Long bookId) {
        BorrowRecord record = new BorrowRecord();
        record.userId = userId;
        record.bookId = bookId;
        record.status = 1;
        record.borrowDate = LocalDateTime.now();
        record.dueDate = LocalDateTime.now().plusDays(30); // 默认30天借阅期
        record.renewCount = 0;
        record.fineAmount = BigDecimal.ZERO;
        return record;
    }

    // 领域行为：归还图书
    public void returnBook() {
        this.status = 2;
        this.returnDate = LocalDateTime.now();
        // 计算超时费
        calculateFine();
    }

    // 领域行为：计算超时费（每天1元）
    private void calculateFine() {
        if (returnDate.isAfter(dueDate)) {
            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            if (overdueDays > 0) {
                this.fineAmount = BigDecimal.valueOf(overdueDays);
            }
        }
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBookId() { return bookId; }
    public LocalDateTime getBorrowDate() { return borrowDate; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getReturnDate() { return returnDate; }
    public Integer getStatus() { return status; }
    public Integer getRenewCount() { return renewCount; }
    public BigDecimal getFineAmount() { return fineAmount; }
    public LocalDateTime getCreateTime() { return createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }

    // Setters (用于从PO转换)
    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }
    public void setBorrowDate(LocalDateTime borrowDate) { this.borrowDate = borrowDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public void setStatus(Integer status) { this.status = status; }
    public void setRenewCount(Integer renewCount) { this.renewCount = renewCount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
