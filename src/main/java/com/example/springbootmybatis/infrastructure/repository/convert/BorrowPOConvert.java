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
        po.setBorrowDate(record.getBorrowDate());
        po.setDueDate(record.getDueDate());
        po.setReturnDate(record.getReturnDate());
        po.setStatus(record.getStatus());
        po.setRenewCount(record.getRenewCount());
        po.setFineAmount(record.getFineAmount());
        po.setCreateTime(record.getCreateTime());
        po.setUpdateTime(record.getUpdateTime());
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
        record.setBorrowDate(po.getBorrowDate());
        record.setDueDate(po.getDueDate());
        record.setReturnDate(po.getReturnDate());
        record.setStatus(po.getStatus());
        record.setRenewCount(po.getRenewCount());
        record.setFineAmount(po.getFineAmount());
        record.setCreateTime(po.getCreateTime());
        record.setUpdateTime(po.getUpdateTime());
        return record;
    }
}
