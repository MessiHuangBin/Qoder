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
        //测试556677
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
