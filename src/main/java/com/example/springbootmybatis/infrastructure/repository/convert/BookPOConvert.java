package com.example.springbootmybatis.infrastructure.repository.convert;

import com.example.springbootmybatis.domain.model.entity.Book;
import com.example.springbootmybatis.infrastructure.repository.po.BookPO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Book领域对象与PO转换器
 */
@Component
public class BookPOConvert {

    public Book toDomain(BookPO po) {
        if (po == null) {
            return null;
        }
        Book book = new Book();
        book.setId(po.getId());
        book.setIsbn(po.getIsbn());
        book.setTitle(po.getTitle());
        book.setAuthor(po.getAuthor());
        book.setPublisher(po.getPublisher());
        book.setPublishDate(po.getPublishDate());
        book.setCategoryId(po.getCategoryId());
        book.setLocation(po.getLocation());
        book.setTotalCount(po.getTotalCount());
        book.setAvailableCount(po.getAvailableCount());
        book.setStatus(po.getStatus());
        book.setDescription(po.getDescription());
        book.setCreateTime(po.getCreateTime());
        book.setUpdateTime(po.getUpdateTime());
        book.setDeleted(po.getDeleted());
        return book;
    }

    public BookPO toPO(Book book) {
        if (book == null) {
            return null;
        }
        BookPO po = new BookPO();
        po.setId(book.getId());
        po.setIsbn(book.getIsbn());
        po.setTitle(book.getTitle());
        po.setAuthor(book.getAuthor());
        po.setPublisher(book.getPublisher());
        po.setPublishDate(book.getPublishDate());
        po.setCategoryId(book.getCategoryId());
        po.setLocation(book.getLocation());
        po.setTotalCount(book.getTotalCount());
        po.setAvailableCount(book.getAvailableCount());
        po.setStatus(book.getStatus());
        po.setDescription(book.getDescription());
        po.setCreateTime(book.getCreateTime());
        po.setUpdateTime(book.getUpdateTime());
        po.setDeleted(book.getDeleted());
        return po;
    }

    public List<Book> toDomainList(List<BookPO> poList) {
        if (poList == null) {
            return null;
        }
        List<Book> bookList = new ArrayList<>();
        for (BookPO po : poList) {
            bookList.add(toDomain(po));
        }
        return bookList;
    }
}
