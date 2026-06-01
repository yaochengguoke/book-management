package com.example.book.service;

import com.example.book.dto.BookRequest;
import com.example.book.dto.BookResponse;
import com.example.book.entity.Book;
import com.example.book.exception.ResourceNotFoundException;
import com.example.book.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookResponse::new);
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
        return new BookResponse(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> searchBooks(String title, String author, Pageable pageable) {
        if (title != null && author != null) {
            return bookRepository.findByTitleContainingAndAuthorContaining(title, author, pageable)
                    .map(BookResponse::new);
        } else if (title != null) {
            return bookRepository.findByTitleContaining(title, pageable).map(BookResponse::new);
        } else if (author != null) {
            return bookRepository.findByAuthorContaining(author, pageable).map(BookResponse::new);
        } else {
            return getAllBooks(pageable);
        }
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setQuantity(request.getQuantity());
        book.setPrice(request.getPrice());
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        Book savedBook = bookRepository.save(book);
        return new BookResponse(savedBook);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setQuantity(request.getQuantity());
        book.setPrice(request.getPrice());
        book.setUpdatedAt(LocalDateTime.now());

        Book updatedBook = bookRepository.save(book);
        return new BookResponse(updatedBook);
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book", "id", id);
        }
        bookRepository.deleteById(id);
    }
}