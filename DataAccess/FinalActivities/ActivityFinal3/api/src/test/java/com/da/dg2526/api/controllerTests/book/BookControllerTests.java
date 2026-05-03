package com.da.dg2526.api.controllerTests.book;

import com.da.dg2526.api.controllers.BookController;
import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.dto.bookEntity.*;
import com.da.dg2526.api.models.dto.reserveEntity.ReserveResultDTO;
import com.da.dg2526.api.models.enums.Status;
import com.da.dg2526.api.services.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BookController bookController;
    @Autowired
    private BookService bookService;

    private final String serviceError = "Service Error";
    private final String testUserId = "testUserId";
    private final String testBookId = "testBookId";
    private final BookNewInputDTO testBookInputDTO = new BookNewInputDTO("testBookId", "test", 2, "test book if seen on production something went wrong", "test publisher", "OTHER");

    // Adding books.
    @Test
    void addBook_success() throws Exception {
        var response = new BookNewInputResultDTO(testBookInputDTO.isbn(), testBookInputDTO.title(), testBookInputDTO.copies(), testBookInputDTO.outline(), testBookInputDTO.publisher(), testBookInputDTO.category());

        when(bookService.addNewBook(testBookInputDTO)).thenReturn(response);

        var result = bookController.addBook(testBookInputDTO);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void addBook_serviceException_failure() {
        when(bookService.addNewBook(testBookInputDTO)).thenThrow(new ServiceValidationException(serviceError));

        var result = bookController.addBook(testBookInputDTO);
        assertEquals(400, result.getStatusCode().value());
        assertEquals(serviceError, result.getBody());
    }


    // Lendings
    @Test
    void lendBook_success() {
        var response = new BookLendingResponseDTO(Status.SUCCESS, "", null, false);

        when(bookService.lendBook(testBookId, testUserId)).thenReturn(response);

        var result = bookController.lendBook(testBookId, testUserId);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void lendBook_failure() {
        var response = new BookLendingResponseDTO(Status.FAILURE, "", null, false);
        when(bookService.lendBook(testBookId, testUserId)).thenReturn(response);

        var result = bookController.lendBook(testBookId, testUserId);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void lendBook_serviceException_failure() {
        when(bookService.lendBook(testBookId, testUserId)).thenThrow(new ServiceValidationException(serviceError));

        var result = bookController.lendBook(testBookId, testUserId);
        assertEquals(400, result.getStatusCode().value());
        assertEquals(serviceError, result.getBody());
    }

    @Test
    void returnBook_success() {
        var response = new BookReturnResponseDTO(Status.SUCCESS, "", false);

        when(bookService.returnBook(testBookId, testUserId)).thenReturn(response);

        var result = bookController.returnBook(testBookId, testUserId);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void returnBook_failure() {
        var response = new BookReturnResponseDTO(Status.FAILURE, "", false);

        when(bookService.returnBook(testBookId, testUserId)).thenReturn(response);

        var result = bookController.returnBook(testBookId, testUserId);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void returnBook_serviceException_failure() {
        when(bookService.returnBook(testBookId, testUserId)).thenThrow(new ServiceValidationException(serviceError));

        var result = bookController.returnBook(testBookId, testUserId);
        assertEquals(400, result.getStatusCode().value());
        assertEquals(serviceError, result.getBody());
    }

    @Test
    void reserveBook_success() {
        var body = new ReserveResultDTO(null, testUserId, 999);
        var response = new BookReserveResponseDTO(Status.SUCCESS, "", body);

        when(bookService.reserveBook(testBookId, testUserId)).thenReturn(response);
        var result = bookController.reserveBook(testBookId, testUserId);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void reserveBook_failure() {
        var response = new BookReserveResponseDTO(Status.FAILURE, "", null);
        when(bookService.reserveBook(testBookId, testUserId)).thenReturn(response);

        var result = bookController.reserveBook(testBookId, testUserId);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void reserveBook_serviceException_failure() {
        when(bookController.reserveBook(testBookId, testUserId)).thenThrow(new ServiceValidationException(serviceError));
        var result = bookController.reserveBook(testBookId, testUserId);

        assertEquals(400, result.getStatusCode().value());
        assertEquals(serviceError, result.getBody());
    }

}
