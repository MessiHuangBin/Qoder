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
        BorrowRecord updatedRecord = borrowDomainService.returnBook(borrowRecordId);
        
        // 更新图书可借数量
        Book book = bookRepository.findById(updatedRecord.getBookId());
        if (book != null) {
            book.returnBook();
            bookRepository.update(book);
        }
        
        // 更新用户借阅数量
        User user = userRepository.findById(updatedRecord.getUserId());
        if (user != null && user.getCurrentBorrowCount() > 0) {
            user.setCurrentBorrowCount(user.getCurrentBorrowCount() - 1);
            userRepository.update(user);
        }
        
        return BorrowConvert.toDTO(updatedRecord);
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
