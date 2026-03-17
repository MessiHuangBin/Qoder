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
        dto.setBorrowDate(record.getBorrowDate());
        dto.setDueDate(record.getDueDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus());
        dto.setRenewCount(record.getRenewCount());
        dto.setFineAmount(record.getFineAmount());
        return dto;
    }
}
