package com.example.springbootmybatis.domain.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 图书领域实体
 */
public class Book {
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

    // 领域行为方法
    public static Book create(String isbn, String title, String author, String publisher, 
                              LocalDate publishDate, Long categoryId, String location, 
                              Integer totalCount, String description) {
        Book book = new Book();
        book.isbn = isbn;
        book.title = title;
        book.author = author;
        book.publisher = publisher;
        book.publishDate = publishDate;
        book.categoryId = categoryId;
        book.location = location;
        book.totalCount = totalCount != null ? totalCount : 1;
        book.availableCount = book.totalCount;
        book.status = 1;
        book.description = description;
        book.deleted = 0;
        book.createTime = LocalDateTime.now();
        book.updateTime = LocalDateTime.now();
        return book;
    }

    public void updateInfo(String title, String author, String publisher, 
                           LocalDate publishDate, Long categoryId, String location,
                           String description) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.categoryId = categoryId;
        this.location = location;
        this.description = description;
        this.updateTime = LocalDateTime.now();
    }

    public void borrow() {
        if (this.availableCount > 0) {
            this.availableCount--;
        }
    }

    public void returnBook() {
        if (this.availableCount < this.totalCount) {
            this.availableCount++;
        }
    }

    public void deactivate() {
        this.status = 0;
        this.updateTime = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public LocalDate getPublishDate() { return publishDate; }
    public Long getCategoryId() { return categoryId; }
    public String getLocation() { return location; }
    public Integer getTotalCount() { return totalCount; }
    public Integer getAvailableCount() { return availableCount; }
    public Integer getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getCreateTime() { return createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public Integer getDeleted() { return deleted; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setLocation(String location) { this.location = location; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public void setAvailableCount(Integer availableCount) { this.availableCount = availableCount; }
    public void setStatus(Integer status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
