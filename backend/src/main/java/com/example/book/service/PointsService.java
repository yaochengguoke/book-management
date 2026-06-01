package com.example.book.service;

import com.example.book.dto.PointsLogResponse;
import com.example.book.dto.PointsResponse;
import com.example.book.entity.Points;
import com.example.book.entity.PointsLog;
import com.example.book.entity.User;
import com.example.book.exception.ResourceNotFoundException;
import com.example.book.repository.PointsLogRepository;
import com.example.book.repository.PointsRepository;
import com.example.book.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class PointsService {

    private final PointsRepository pointsRepository;
    private final PointsLogRepository pointsLogRepository;
    private final UserRepository userRepository;

    private static final Map<String, String> TYPE_MAP = new HashMap<>();
    static {
        TYPE_MAP.put("EARN_BORROW", "借阅获得");
        TYPE_MAP.put("EARN_RETURN", "归还获得");
        TYPE_MAP.put("EARN_SIGNIN", "签到获得");
        TYPE_MAP.put("SPEND_EXCHANGE", "积分兑换");
        TYPE_MAP.put("SPEND_PENALTY", "逾期扣除");
        TYPE_MAP.put("ADMIN_ADD", "管理员添加");
        TYPE_MAP.put("ADMIN_SUBTRACT", "管理员扣除");
    }

    public PointsService(PointsRepository pointsRepository,
                        PointsLogRepository pointsLogRepository,
                        UserRepository userRepository) {
        this.pointsRepository = pointsRepository;
        this.pointsLogRepository = pointsLogRepository;
        this.userRepository = userRepository;
    }

    public PointsResponse getPointsByUserId(Long userId) {
        Points points = pointsRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户积分记录不存在"));
        return convertToResponse(points);
    }

    @Transactional
    public PointsResponse addPoints(Long userId, Integer amount, String type, String description,
                                   Long relatedId, String relatedType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("用户不存在"));

        Points points = pointsRepository.findByUserId(userId).orElse(new Points(user));
        
        Integer balanceBefore = points.getBalance();
        Integer newBalance = points.getBalance() + amount;
        
        if (newBalance < 0) {
            throw new IllegalStateException("积分余额不足");
        }

        points.setBalance(newBalance);
        points.setTotalEarned(points.getTotalEarned() + (amount > 0 ? amount : 0));
        points.setTotalSpent(points.getTotalSpent() + (amount < 0 ? Math.abs(amount) : 0));
        
        Points savedPoints = pointsRepository.save(points);

        PointsLog log = new PointsLog(user, amount, type, description, 
                balanceBefore, newBalance, relatedId, relatedType);
        pointsLogRepository.save(log);

        return convertToResponse(savedPoints);
    }

    @Transactional
    public PointsResponse earnPointsForBorrow(Long userId, Long borrowId) {
        return addPoints(userId, 10, "EARN_BORROW", "借阅图书获得10积分", borrowId, "BORROW");
    }

    @Transactional
    public PointsResponse earnPointsForReturn(Long userId, Long borrowId) {
        return addPoints(userId, 5, "EARN_RETURN", "归还图书获得5积分", borrowId, "BORROW");
    }

    @Transactional
    public PointsResponse signIn(Long userId) {
        return addPoints(userId, 1, "EARN_SIGNIN", "每日签到获得1积分", null, null);
    }

    @Transactional
    public PointsResponse deductPoints(Long userId, Integer amount, String description) {
        return addPoints(userId, -amount, "SPEND_EXCHANGE", description, null, null);
    }

    public Page<PointsLogResponse> getPointsLogs(Long userId, Pageable pageable) {
        return pointsLogRepository.findByUserId(userId, pageable).map(this::convertToLogResponse);
    }

    private PointsResponse convertToResponse(Points points) {
        return new PointsResponse(
                points.getUser().getId(),
                points.getUser().getUsername(),
                points.getBalance(),
                points.getTotalEarned(),
                points.getTotalSpent()
        );
    }

    private PointsLogResponse convertToLogResponse(PointsLog log) {
        return new PointsLogResponse(
                log.getId(),
                log.getUser().getId(),
                log.getUser().getUsername(),
                log.getAmount(),
                log.getType(),
                TYPE_MAP.getOrDefault(log.getType(), log.getType()),
                log.getDescription(),
                log.getBalanceBefore(),
                log.getBalanceAfter(),
                log.getCreatedAt()
        );
    }
}