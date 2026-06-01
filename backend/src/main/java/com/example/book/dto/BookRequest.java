package com.example.book.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class BookRequest {

    @NotBlank(message = "书名不能为空")
    @Size(max = 256, message = "书名长度不能超过256个字符")
    private String title;

    @NotBlank(message = "作者不能为空")
    @Size(max = 128, message = "作者长度不能超过128个字符")
    private String author;

    @Size(max = 128, message = "出版社长度不能超过128个字符")
    private String publisher;

    @Size(max = 64, message = "ISBN长度不能超过64个字符")
    private String isbn;

    @Size(max = 2000, message = "描述长度不能超过2000个字符")
    private String description;

    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须为正数")
    private Integer quantity;

    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须为正数")
    private Double price;

    public BookRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}