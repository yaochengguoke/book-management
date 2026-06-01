package com.example.book.repository;

import com.example.book.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Reservation> findByBookIdAndStatus(Long bookId, String status);
    List<Reservation> findByStatus(String status);
    long countByBookIdAndStatus(Long bookId, String status);
    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status);
}
