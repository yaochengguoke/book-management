package com.example.book.repository;

import com.example.book.entity.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointsRepository extends JpaRepository<Points, Long> {

    Optional<Points> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}