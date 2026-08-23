package com.arjun.ai_service.bookstore.ask;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookAskController {

    private final BookAskService askService;

    public BookAskController(BookAskService askService){
        this.askService = askService;
    }

    public record AskRequest(@NotBlank String question){}

    @PostMapping("/ask")
    public BookAskResponse ask (@RequestBody @Validated AskRequest request){
        return askService.ask(request.question);
    }

}
