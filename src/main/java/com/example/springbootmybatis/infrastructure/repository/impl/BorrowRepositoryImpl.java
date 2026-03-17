package com.example.springbootmybatis.infrastructure.repository.impl;

import com.example.springbootmybatis.domain.model.entity.BorrowRecord;
import com.example.springbootmybatis.domain.support.BorrowRepository;
import com.example.springbootmybatis.infrastructure.repository.convert.BorrowPOConvert;
import com.example.springbootmybatis.infrastructure.repository.mapper.BorrowMapper;
import com.example.springbootmybatis.infrastructure.repository.po.BorrowRecordPO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 借阅记录仓储实现
 */
@Repository
public class BorrowRepositoryImpl implements BorrowRepository {
    
    private final BorrowMapper borrowMapper;
    
    public BorrowRepositoryImpl(BorrowMapper borrowMapper) {
        this.borrowMapper = borrowMapper;
    }
    
    @Override
    public void save(BorrowRecord record) {
        BorrowRecordPO po = BorrowPOConvert.toPO(record);
        borrowMapper.insert(po);
        record.setId(po.getId());
    }
    
    @Override
    public BorrowRecord findById(Long id) {
        BorrowRecordPO po = borrowMapper.selectById(id);
        return BorrowPOConvert.toEntity(po);
    }
    
    @Override
    public void update(BorrowRecord record) {
        BorrowRecordPO po = BorrowPOConvert.toPO(record);
        borrowMapper.update(po);
    }
    
    @Override
    public List<BorrowRecord> findByUserIdAndStatus(Long userId, Integer status) {
        List<BorrowRecordPO> poList = borrowMapper.selectByUserIdAndStatus(userId, status);
        return poList.stream()
                .map(BorrowPOConvert::toEntity)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<BorrowRecord> findByBookIdAndStatus(Long bookId, Integer status) {
        List<BorrowRecordPO> poList = borrowMapper.selectByBookIdAndStatus(bookId, status);
        return poList.stream()
                .map(BorrowPOConvert::toEntity)
                .collect(Collectors.toList());
    }
}
