package com.example.book.repository;

import com.example.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findByTitleContaining(String title, Pageable pageable);

    Page<Book> findByAuthorContaining(String author, Pageable pageable);

    Page<Book> findByTitleContainingAndAuthorContaining(String title, String author, Pageable pageable);

    List<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    Page<Book> findByCategory(String category, Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT b.category FROM Book b WHERE b.category IS NOT NULL AND b.category <> ''")
    List<String> findDistinctCategories();
}