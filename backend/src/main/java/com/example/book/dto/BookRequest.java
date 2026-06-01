package com.example.book.dto;

import javax.validation.constraints.*;

public class BookRequest {

    @NotBlank(message = "书名不能为空")
    @Size(max = 256) private String title;

    @NotBlank(message = "作者不能为空")
    @Size(max = 128) private String author;

    @Size(max = 128) private String publisher;
    @Size(max = 64) private String isbn;
    @Size(max = 2000) private String description;
    @NotNull @Positive private Integer quantity;
    @NotNull @Positive private Double price;
    private String category;
    private String coverImage;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
}
