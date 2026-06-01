package com.example.book.service;

import com.example.book.dto.BorrowRequest;
import com.example.book.dto.BorrowResponse;
import com.example.book.entity.Book;
import com.example.book.entity.Borrow;
import com.example.book.entity.User;
import com.example.book.exception.ResourceNotFoundException;
import com.example.book.repository.BookRepository;
import com.example.book.repository.BorrowRepository;
import com.example.book.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BorrowService(BorrowRepository borrowRepository,
                         BookRepository bookRepository,
                         UserRepository userRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BorrowResponse borrowBook(BorrowRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("图书不存在"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        if (book.getQuantity() <= 0) {
            throw new IllegalStateException("该图书库存不足");
        }

        List<Borrow> activeBorrows = borrowRepository.findActiveBorrowsByBookId(book.getId());
        if (activeBorrows.size() >= book.getQuantity()) {
            throw new IllegalStateException("该图书已全部借出");
        }

        LocalDateTime borrowDate = LocalDateTime.now();
        LocalDateTime dueDate = borrowDate.plusDays(request.getBorrowDays() != null ? request.getBorrowDays() : 14);

        Borrow borrow = new Borrow(book, user, borrowDate, dueDate);
        borrow = borrowRepository.save(borrow);

        return convertToResponse(borrow);
    }

    @Transactional
    public BorrowResponse returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("借阅记录不存在"));

        if (!"BORROWED".equals(borrow.getStatus())) {
            throw new IllegalStateException("该借阅记录状态不正确");
        }

        borrow.setReturnDate(LocalDateTime.now());
        borrow.setStatus("RETURNED");
        borrow = borrowRepository.save(borrow);

        return convertToResponse(borrow);
    }

    public Page<BorrowResponse> getAllBorrows(Pageable pageable) {
        return borrowRepository.findAll(pageable).map(this::convertToResponse);
    }

    public Page<BorrowResponse> searchBorrows(Long userId, Long bookId, String status, Pageable pageable) {
        return borrowRepository.searchBorrows(userId, bookId, status, pageable).map(this::convertToResponse);
    }

    public Page<BorrowResponse> searchByKeyword(String keyword, Pageable pageable) {
        return borrowRepository.searchByKeyword(keyword, pageable).map(this::convertToResponse);
    }

    public BorrowResponse getBorrowById(Long id) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("借阅记录不存在"));
        return convertToResponse(borrow);
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalBorrowed", borrowRepository.countByStatus("BORROWED"));
        stats.put("totalReturned", borrowRepository.countByStatus("RETURNED"));
        stats.put("totalOverdue", borrowRepository.findOverdueBorrows().size());
        return stats;
    }

    private BorrowResponse convertToResponse(Borrow borrow) {
        return new BorrowResponse(
                borrow.getId(),
                borrow.getBook().getId(),
                borrow.getBook().getTitle(),
                borrow.getBook().getAuthor(),
                borrow.getUser().getId(),
                borrow.getUser().getUsername(),
                borrow.getBorrowDate(),
                borrow.getDueDate(),
                borrow.getReturnDate(),
                borrow.getStatus()
        );
    }
}