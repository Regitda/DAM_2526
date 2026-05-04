package com.da.dg2526.api.controllers;

import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.services.BookService;
import com.da.dg2526.api.utils.LoggerUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/books")
public class BookController {


    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Values validated respective to DB rules.
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody @Valid BookNewInputDTO dto) {
        LoggerUtil.logInfo("Adding book");
        var response = bookService.addNewBook(dto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/import")
    public ResponseEntity<?> addBooks(@RequestBody @Valid List<@Valid BookNewInputDTO> dto) {
        var response = bookService.importBooks(dto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{isbn}/lend")
    public ResponseEntity<?> lendBook(@PathVariable @NotBlank @Size(max = 13) String isbn, @RequestParam @NotBlank @Size(max = 8) String userId) {
        LoggerUtil.logInfo("Lending book");
        var response = bookService.lendBook(isbn, userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{isbn}/reserve")
    public ResponseEntity<?> reserveBook(@PathVariable @NotBlank @Size(max = 13) String isbn, @RequestParam @NotBlank @Size(max = 8) String userId) {
        LoggerUtil.logInfo("Reserving book");
        var response = bookService.reserveBook(isbn, userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{isbn}/return")
    public ResponseEntity<?> returnBook(@PathVariable @NotBlank @Size(max = 13) String isbn, @RequestParam @NotBlank @Size(max = 8) String userId) {
        LoggerUtil.logInfo("Returning book");
        var response = bookService.returnBook(isbn, userId);
        return ResponseEntity.ok().body(response);
    }

}
