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
        assertNotNull(updated.getReturnDate());
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
