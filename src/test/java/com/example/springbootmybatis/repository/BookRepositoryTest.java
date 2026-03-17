package com.example.springbootmybatis.repository;

import com.example.springbootmybatis.domain.model.entity.Book;
import com.example.springbootmybatis.domain.support.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testFindById() {
        // 测试查询id为28的图书
        Book book = bookRepository.findById(28L);
        
        // 验证结果
        assertNotNull(book, "图书不应该为空");
        assertEquals(28L, book.getId(), "ID应该为28");
        assertEquals("Java核心技术 卷I", book.getTitle(), "书名应该匹配");
        assertEquals("Cay S. Horstmann", book.getAuthor(), "作者应该匹配");
        assertEquals("机械工业出版社", book.getPublisher(), "出版社应该匹配");
        assertEquals("9787111255833", book.getIsbn(), "ISBN应该匹配");
        
        System.out.println("测试通过！查询到的图书信息：");
        System.out.println("ID: " + book.getId());
        System.out.println("书名: " + book.getTitle());
        System.out.println("作者: " + book.getAuthor());
        System.out.println("出版社: " + book.getPublisher());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("位置: " + book.getLocation());
        System.out.println("总数量: " + book.getTotalCount());
        System.out.println("可借数量: " + book.getAvailableCount());
    }
}
