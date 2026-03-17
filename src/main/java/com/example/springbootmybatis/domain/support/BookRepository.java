package com.example.springbootmybatis.domain.support;

import com.example.springbootmybatis.domain.model.entity.Book;
import java.util.List;

/**
 * 图书仓储接口
 */
public interface BookRepository {
    
    Book findById(Long id);
    
    Book findByIsbn(String isbn);
    
    List<Book> findByTitle(String title);
    
    List<Book> findByAuthor(String author);
    
    List<Book> findByCategoryId(Long categoryId);
    
    List<Book> findByStatus(Integer status);
    
    List<Book> findAll();
    
    void save(Book book);
    
    void update(Book book);
    
    void delete(Long id);
}
