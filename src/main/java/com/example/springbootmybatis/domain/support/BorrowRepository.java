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
