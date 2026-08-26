package com.arjun.library_service.bookstore.library.card;

import com.arjun.library_service.bookstore.library.card.dto.AccessCardResponse;
import com.arjun.library_service.bookstore.library.card.dto.CardStatusResponse;
import com.arjun.library_service.bookstore.library.card.dto.IssueCardRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library/cards")
@Tag(name = "Access Cards", description = "Library access card management")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Issue a new access card")
    AccessCardResponse issueCard(@Valid @RequestBody IssueCardRequest request) {
        return cardService.issueCard(request);
    }

    @GetMapping("/{cardId}")
    @Operation(summary = "Get access card by ID")
    AccessCardResponse getById(@PathVariable String cardId) {
        return cardService.getById(cardId);
    }

    @PatchMapping("/{cardId}/block")
    @Operation(summary = "Block an access card")
    AccessCardResponse blockCard(@PathVariable String cardId) {
        return cardService.blockCard(cardId);
    }

    @GetMapping("/{cardId}/status")
    @Operation(summary = "Get card entry status")
    CardStatusResponse getStatus(@PathVariable String cardId) {
        return cardService.getStatus(cardId);
    }
}
