package com.arjun.library_service.bookstore.library.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<LoanEntity, Long> {

    List<LoanEntity> findByUserIdAndStatus(Long userId, LoanStatus status);

    List<LoanEntity> findByUserId(Long userId);

    List<LoanEntity> findByDueAtBeforeAndStatus(LocalDateTime dueAt, LoanStatus status);
}
