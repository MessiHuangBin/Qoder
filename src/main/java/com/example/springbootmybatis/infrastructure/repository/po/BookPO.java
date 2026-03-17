package com.example.springbootmybatis.infrastructure.repository.po;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书持久化对象
 */
@Data
public class BookPO {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private LocalDate publishDate;
    private Long categoryId;
    private String location;
    private Integer totalCount;
    private Integer availableCount;
    private Integer status;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer deleted;
}
