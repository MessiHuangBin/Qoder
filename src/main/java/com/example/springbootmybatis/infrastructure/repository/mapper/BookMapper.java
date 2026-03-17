package com.example.springbootmybatis.infrastructure.repository.mapper;

import com.example.springbootmybatis.infrastructure.repository.po.BookPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 图书Mapper
 */
@Mapper
public interface BookMapper {

    BookPO selectById(@Param("id") Long id);

    BookPO selectByIsbn(@Param("isbn") String isbn);

    List<BookPO> selectByTitle(@Param("title") String title);

    List<BookPO> selectByAuthor(@Param("author") String author);

    List<BookPO> selectByCategoryId(@Param("categoryId") Long categoryId);

    List<BookPO> selectByStatus(@Param("status") Integer status);

    List<BookPO> selectAll();

    int insert(BookPO bookPO);

    int update(BookPO bookPO);

    int deleteById(@Param("id") Long id);
}
