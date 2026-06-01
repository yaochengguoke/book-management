package com.example.book.repository;

import com.example.book.entity.PointsLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointsLogRepository extends JpaRepository<PointsLog, Long> {

    Page<PointsLog> findByUserId(Long userId, Pageable pageable);

    List<PointsLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserId(Long userId);
}