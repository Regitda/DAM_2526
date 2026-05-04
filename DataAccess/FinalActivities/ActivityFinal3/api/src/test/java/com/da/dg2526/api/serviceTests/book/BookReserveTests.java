package com.da.dg2526.api.serviceTests.book;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.entities.ReservationEntity;
import com.da.dg2526.api.models.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookReserveTests extends AbstractBookServiceTestBase {

    @Test
    void reserveBook_noReservations_success() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        testUser.setPhone("0");
        testUser.setEmail("email");

        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBook)).thenReturn(testBook.getCopies());
        when(reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(testBook)).thenReturn(0);

        var result = bookService.reserveBook(testBookId, testUserId);

        verify(reservationEntityDAO).save(any(ReservationEntity.class));

        assertEquals(Status.SUCCESS, result.status());
        assertEquals(testUserId, result.reserveResultDTO().BorrowerId());

        // Checking if reservation value is passed
        assertEquals(1, result.reserveResultDTO().amountOfReservationsOnThatBook());
    }

    // Practically the same as above, but should not fail even on free copies due to reservations existing.
    @Test
    void reserveBook_freeCopiesButExistingReservations_success() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        testUser.setPhone("0");
        testUser.setEmail("email");

        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBook)).thenReturn(0);
        when(reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(testBook)).thenReturn(999);

        var result = bookService.reserveBook(testBookId, testUserId);

        verify(reservationEntityDAO).save(any(ReservationEntity.class));

        assertEquals(Status.SUCCESS, result.status());
        assertEquals(testUserId, result.reserveResultDTO().BorrowerId());

        // Checking if reservation value is passed
        assertEquals(1000, result.reserveResultDTO().amountOfReservationsOnThatBook());
    }

    @Test
    void reserveBook_NoEmailOrPhone_failure() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        var exception = assertThrows(ServiceValidationException.class, () -> bookService.reserveBook(testBookId, testUserId));

        assertEquals(createErrorUserPersonalDetailsMissing(testUserId), exception.getMessage());

        verify(reservationEntityDAO, never()).save(any(ReservationEntity.class));
    }

    @Test
    void reserveBook_UserFined_failure() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);
        testUser.setFined(LocalDate.now());

        var result = bookService.reserveBook(testBookId, testUserId);

        assertEquals(Status.FAILURE, result.status());
        assertNull(result.reserveResultDTO());

        verify(reservationEntityDAO, never()).save(any(ReservationEntity.class));
    }

    @Test
    void reserveBook_FreeCopiesAndNoReservations_failure() {
        var testUser = createUser();
        var testBook = createBook();
        mockBookAndUser(testBook, testUser);

        testUser.setPhone("0");
        testUser.setEmail("email");

        // There are copies and reservations are at 0
        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBook)).thenReturn(0);
        when(reservationEntityDAO.countAllByBookEntityAndLendingEntityIsNull(testBook)).thenReturn(0);

        var exception = assertThrows(ServiceValidationException.class, () -> bookService.reserveBook(testBookId, testUserId));

        assertEquals(createErrorBookHasFreeLendings(testBookId, testBook.getCopies()), exception.getMessage());

        verify(reservationEntityDAO, never()).save(any(ReservationEntity.class));
    }

    // Book valid test
    @Test
    void reserveBook_bookNotFound_failure() {
        mockBookNotFound();
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.reserveBook(testBookId, testUserId));

        assertEquals(createErrorBookDoesNotExist(testBookId), exception.getMessage());
    }

    // User valid test.
    @Test
    void reserveBook_userNotFound_failure() {
        mockUserNotFound();
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.reserveBook(testBookId, testUserId));

        assertEquals(createErrorUserDoesNotExist(testUserId), exception.getMessage());
    }

}
