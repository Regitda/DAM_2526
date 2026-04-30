package com.da.dg2526.api.controllers;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.services.BookService;
import com.da.dg2526.api.utils.LoggerUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        try {
            var response = bookService.addNewBook(dto);
            return ResponseEntity.ok().body(response);

        } catch (ServiceValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("{isbn}/lend")
    public ResponseEntity<?> lendBook(@PathVariable String isbn, @RequestParam @NotBlank @Size(max = 8) String userId) {

        LoggerUtil.logInfo("Lending book");
        try {
            var response = bookService.lendBook(isbn, userId);
            return ResponseEntity.ok().body(response);
        } catch (ServiceValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

}
