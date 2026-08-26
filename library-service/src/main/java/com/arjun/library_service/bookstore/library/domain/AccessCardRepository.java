package com.arjun.library_service.bookstore.library.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccessCardRepository extends JpaRepository<AccessCardEntity, String> {

    Optional<AccessCardEntity> findByCardId(String cardId);

    List<AccessCardEntity> findByUserId(Long userId);

    @Query(value = "SELECT nextval('access_card_seq')", nativeQuery = true)
    Long nextCardSequence();
}
