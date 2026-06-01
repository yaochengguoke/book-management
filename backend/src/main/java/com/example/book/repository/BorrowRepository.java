package com.example.book.repository;

import com.example.book.entity.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    Page<Borrow> findByStatus(String status, Pageable pageable);

    Page<Borrow> findByUserId(Long userId, Pageable pageable);

    Page<Borrow> findByBookId(Long bookId, Pageable pageable);

    @Query("SELECT b FROM Borrow b WHERE b.status = 'BORROWED' AND b.dueDate < CURRENT_TIMESTAMP")
    List<Borrow> findOverdueBorrows();

    @Query("SELECT b FROM Borrow b WHERE b.book.id = :bookId AND b.status = 'BORROWED'")
    List<Borrow> findActiveBorrowsByBookId(@Param("bookId") Long bookId);

    long countByStatus(String status);

    @Query("SELECT b FROM Borrow b WHERE " +
           "(:userId IS NULL OR b.user.id = :userId) AND " +
           "(:bookId IS NULL OR b.book.id = :bookId) AND " +
           "(:status IS NULL OR b.status = :status)")
    Page<Borrow> searchBorrows(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT b FROM Borrow b WHERE " +
           "b.book.title LIKE %:keyword% OR " +
           "b.user.username LIKE %:keyword%")
    Page<Borrow> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}