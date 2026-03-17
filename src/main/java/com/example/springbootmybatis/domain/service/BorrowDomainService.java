package com.example.springbootmybatis.domain.service;

import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
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
