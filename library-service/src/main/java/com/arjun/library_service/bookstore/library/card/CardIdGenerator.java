package com.arjun.library_service.bookstore.library.card;

import com.arjun.library_service.bookstore.library.domain.AccessCardRepository;
import java.time.Year;
import org.springframework.stereotype.Component;

@Component
public class CardIdGenerator {

    private final AccessCardRepository accessCardRepository;

    public CardIdGenerator(AccessCardRepository accessCardRepository) {
        this.accessCardRepository = accessCardRepository;
    }

    public String nextPermanentCardId() {
        long seq = accessCardRepository.nextCardSequence();
        return "CARD-" + Year.now().getValue() + "-" + String.format("%04d", seq);
    }
}
