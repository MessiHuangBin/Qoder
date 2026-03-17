package com.example.springbootmybatis.infrastructure.repository.impl;

import com.example.springbootmybatis.domain.model.entity.Book;
import com.example.springbootmybatis.domain.support.BookRepository;
import com.example.springbootmybatis.infrastructure.repository.convert.BookPOConvert;
import com.example.springbootmybatis.infrastructure.repository.mapper.BookMapper;
import com.example.springbootmybatis.infrastructure.repository.po.BookPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图书仓储实现类
 */
@Repository
public class BookRepositoryImpl implements BookRepository {

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookPOConvert bookPOConvert;

    @Override
    public Book findById(Long id) {
        BookPO po = bookMapper.selectById(id);
        return bookPOConvert.toDomain(po);
    }

    @Override
    public Book findByIsbn(String isbn) {
        BookPO po = bookMapper.selectByIsbn(isbn);
        return bookPOConvert.toDomain(po);
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<BookPO> poList = bookMapper.selectByTitle(title);
        return bookPOConvert.toDomainList(poList);
    }

    @Override
    public List<Book> findByAuthor(String author) {
        List<BookPO> poList = bookMapper.selectByAuthor(author);
        return bookPOConvert.toDomainList(poList);
    }

    @Override
    public List<Book> findByCategoryId(Long categoryId) {
        List<BookPO> poList = bookMapper.selectByCategoryId(categoryId);
        return bookPOConvert.toDomainList(poList);
    }

    @Override
    public List<Book> findByStatus(Integer status) {
        List<BookPO> poList = bookMapper.selectByStatus(status);
        return bookPOConvert.toDomainList(poList);
    }

    @Override
    public List<Book> findAll() {
        List<BookPO> poList = bookMapper.selectAll();
        return bookPOConvert.toDomainList(poList);
    }

    @Override
    public void save(Book book) {
        BookPO po = bookPOConvert.toPO(book);
        bookMapper.insert(po);
        book.setId(po.getId());
    }

    @Override
    public void update(Book book) {
        BookPO po = bookPOConvert.toPO(book);
        bookMapper.update(po);
    }

    @Override
    public void delete(Long id) {
        bookMapper.deleteById(id);
    }
}
