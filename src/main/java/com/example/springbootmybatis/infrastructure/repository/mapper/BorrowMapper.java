package com.example.springbootmybatis.infrastructure.repository.mapper;

import com.example.springbootmybatis.infrastructure.repository.po.BorrowRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 借阅记录Mapper
 */
@Mapper
public interface BorrowMapper {
    
    int insert(BorrowRecordPO po);
    
    BorrowRecordPO selectById(@Param("id") Long id);
    
    int update(BorrowRecordPO po);
    
    List<BorrowRecordPO> selectByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
    
    List<BorrowRecordPO> selectByBookIdAndStatus(@Param("bookId") Long bookId, @Param("status") Integer status);
}
