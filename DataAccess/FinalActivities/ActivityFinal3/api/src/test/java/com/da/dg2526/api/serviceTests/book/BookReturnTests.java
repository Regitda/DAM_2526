package com.da.dg2526.api.serviceTests.book;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.entities.LendingEntity;
import com.da.dg2526.api.models.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookReturnTests extends AbstractBookServiceTestBase {


    // Fully valid return test
    @Test
    void returnBook_NotFined_success() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        var lending = new LendingEntity();
        lending.setBook(testBook);
        lending.setBorrower(testUser);
        lending.setLendingdate(LocalDate.now());

        when(lendingEntityDAO.findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(testBook, testUser)).thenReturn(Optional.of(lending));

        var result = bookService.returnBook(testBookId, testUserId);

        assertEquals(Status.SUCCESS, result.status());
        assertFalse(result.lateReturn());
        assertEquals(createSuccessBookReturnedMessage(testBook, false), result.message());


        assertNotNull(lending.getBook());
    }

    // Late return test
    @Test
    void returnBook_Fined_success() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        var lending = new LendingEntity();
        lending.setBook(testBook);
        lending.setBorrower(testUser);
        lending.setLendingdate(LocalDate.now().minusDays(30));


        when(lendingEntityDAO.findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(testBook, testUser)).thenReturn(Optional.of(lending));
        var result = bookService.returnBook(testBookId, testUserId);

        // Check of result code, late return value and message.
        assertEquals(Status.SUCCESS, result.status());
        assertTrue(result.lateReturn());
        assertEquals(createSuccessBookReturnedMessage(testBook, true), result.message());

        // Book should be null.
        assertNotNull(lending.getBook());
    }

    // Lending not found test
    @Test
    void returnBook_LendingNotFound_failure() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        when(lendingEntityDAO.findFirstByBookEntityAndBorrowerAndReturningdateIsNullOrderByIdDesc(testBook, testUser)).thenReturn(Optional.empty());
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.returnBook(testBookId, testUserId));

        assertEquals(createErrorLendingNotFoundMessage(testBookId, testUserId), exception.getMessage());

        // Test user was unfined, if somehow they are now something is wrong.
        assertNull(testUser.getFined());

    }

    // Book valid test
    @Test
    void returnBook_bookNotFound_failure() {
        mockBookNotFound();
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.returnBook(testBookId, testUserId));

        assertEquals(createErrorBookDoesNotExist(testBookId), exception.getMessage());
    }

    // User valid test.
    @Test
    void returnBook_userNotFound_failure() {
        mockUserNotFound();
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.returnBook(testBookId, testUserId));

        assertEquals(createErrorUserDoesNotExist(testUserId), exception.getMessage());
    }

}
