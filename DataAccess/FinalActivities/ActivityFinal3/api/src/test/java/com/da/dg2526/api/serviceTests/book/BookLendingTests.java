package com.da.dg2526.api.serviceTests.book;

import com.da.dg2526.api.exceptions.ServiceValidationException;
import com.da.dg2526.api.models.entities.LendingEntity;
import com.da.dg2526.api.models.entities.ReservationEntity;
import com.da.dg2526.api.models.entities.UserEntity;
import com.da.dg2526.api.models.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static com.da.dg2526.api.services.ServiceErrorMessages.*;


@ExtendWith(MockitoExtension.class)
public class BookLendingTests extends AbstractBookServiceTestBase {

    // Unfined user. With no lendings
    @Test
    void lendBook_success() {
        var testUserEntity = createUser();
        var testBookEntity = createBook();
        mockBookAndUser(testBookEntity, testUserEntity);

        // Book not lent and user has not borrowed any books
        when(lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(testUserEntity)).thenReturn(0);
        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBookEntity)).thenReturn(0);

        // No reservation
        when(reservationEntityDAO.findFirstByBookEntityAndLendingEntityIsNullOrderByDateAsc(testBookEntity)).thenReturn(Optional.empty());

        var result = bookService.lendBook(testBookId, testUserId);

        // Lending must be saved.
        verify(lendingEntityDAO).save(any(LendingEntity.class));

        // If lent happen, you cannot reserve.
        assertFalse(result.canReserve());

        // Success.
        assertEquals(Status.SUCCESS, result.status());

        // Testing DTO returns correct values.
        assertEquals(testUserEntity.getCode(), result.lendingReturnDTO().borrowerCode());
        assertEquals(testBookEntity.getTitle(), result.lendingReturnDTO().bookTitle());
    }

    // User correct, book has reserve by user.
    @Test
    void lendBook_bookHasCopyButReservedByUser_success() {
        var testUserEntity = createUser();
        var testBookEntity = createBook();
        mockBookAndUser(testBookEntity, testUserEntity);

        var testReservationEntity = new ReservationEntity();
        testReservationEntity.setBorrower(testUserEntity);
        testReservationEntity.setBook(testBookEntity);


        // Book not lent and user has not borrowed any books
        when(lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(testUserEntity)).thenReturn(0);
        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBookEntity)).thenReturn(0);

        // Yes reservation with user being the reservation
        when(reservationEntityDAO.findFirstByBookEntityAndLendingEntityIsNullOrderByDateAsc(testBookEntity)).thenReturn(Optional.of(testReservationEntity));

        var result = bookService.lendBook(testBookId, testUserId);

        // Lending must be saved.
        verify(lendingEntityDAO).save(any(LendingEntity.class));

        // If lent happen, you cannot reserve.
        assertFalse(result.canReserve());

        // Success.
        assertEquals(Status.SUCCESS, result.status());

        // Testing DTO returns correct values.
        assertEquals(testUserEntity.getCode(), result.lendingReturnDTO().borrowerCode());
        assertEquals(testBookEntity.getTitle(), result.lendingReturnDTO().bookTitle());

    }

    // User correct, book has reserve by user.
    @Test
    void lendBook_bookHasCopyButReservedByNotUser_failure() {
        var testUserEntity = createUser();
        var testBookEntity = createBook();
        mockBookAndUser(testBookEntity, testUserEntity);

        // Other user who is the owner of the reserve.
        var testOtherUserEntity = new UserEntity();
        testOtherUserEntity.setName("OtherUser");
        testOtherUserEntity.setName("OtherUser");

        var testReservationEntity = new ReservationEntity();
        testReservationEntity.setBorrower(testOtherUserEntity);
        testReservationEntity.setBook(testBookEntity);

        // Book not lent and user has not borrowed any books
        when(lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(testUserEntity)).thenReturn(0);
        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBookEntity)).thenReturn(1);

        // Yes reservation with user being the reservation
        when(reservationEntityDAO.findFirstByBookEntityAndLendingEntityIsNullOrderByDateAsc(testBookEntity)).thenReturn(Optional.of(testReservationEntity));

        var result = bookService.lendBook(testBookId, testUserId);

        // Lending must not save.
        verify(lendingEntityDAO, never()).save(any(LendingEntity.class));

        // If did not happen, you can reserve.
        assertTrue(result.canReserve());

        // Success.
        assertEquals(Status.FAILURE, result.status());

        // Testing DTO is null.
        assertNull(result.lendingReturnDTO());

    }

    // Fined user.
    @Test
    void lendBook_userFined_failure() {

        var testUserEntity = createUser();
        testUserEntity.setFined(LocalDate.now());

        var testBookEntity = createBook();
        mockBookAndUser(testBookEntity, testUserEntity);

        // If user is fined nothing happens outside of this.
        var result = bookService.lendBook(testBookId, testUserId);

        // Lending saving must not have happened.
        verify(lendingEntityDAO, never()).save(any(LendingEntity.class));

        // If fined you cannot reserve.
        assertFalse(result.canReserve());
        assertEquals(Status.FAILURE, result.status());

        // Testing DTO is null
        assertNull(result.lendingReturnDTO());
    }

    // Unfined user. With max lendings
    @Test
    void lendBook_userBorrowedTooManyBooks_failure() {
        var testUserEntity = createUser();
        var testBookEntity = createBook();
        mockBookAndUser(testBookEntity, testUserEntity);


        // Book not lent and user has not borrowed any books
        when(lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(testUserEntity)).thenReturn(3);
        // If user is fined nothing happens outside of this.
        var result = bookService.lendBook(testBookId, testUserId);

        // Lending saving must not have happened.
        verify(lendingEntityDAO, never()).save(any(LendingEntity.class));

        // If you are maxed you cannot reserve more books.
        assertFalse(result.canReserve());
        assertEquals(Status.FAILURE, result.status());

        // Testing DTO is null
        assertNull(result.lendingReturnDTO());

    }

    // Book is available to reserve.
    @Test
    void lendBook_shouldFailAndAllowReserve_whenNoCopiesLeft() {
        // Unfined user. With max lendings
        var testUserEntity = createUser();
        var testBookEntity = createBook();
        mockBookAndUser(testBookEntity, testUserEntity);

        // Book fully lent out and user has not borrowed any books
        when(lendingEntityDAO.countAllByBorrowerAndReturningdateIsNull(testUserEntity)).thenReturn(0);
        when(lendingEntityDAO.countAllByBookEntityAndReturningdateIsNull(testBookEntity)).thenReturn(2);

        // If user is fined nothing happens outside of this.
        var result = bookService.lendBook(testBookId, testUserId);

        // Lending saving must not have happened.
        verify(lendingEntityDAO, never()).save(any(LendingEntity.class));

        // Reserve possible.
        assertTrue(result.canReserve());
        assertEquals(Status.FAILURE, result.status());

        // Testing DTO is null
        assertNull(result.lendingReturnDTO());

    }

    // Book valid test
    @Test
    void lendBook_bookNotFound_failure() {
        mockBookNotFound();
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.lendBook(testBookId, testUserId));

        assertEquals(bookDoesNotExist(testBookId), exception.getMessage());
    }

    // User valid test.
    @Test
    void lendBook_userNotFound_failure() {
        mockUserNotFound();
        var exception = assertThrows(ServiceValidationException.class, () -> bookService.lendBook(testBookId, testUserId));

        assertEquals(userDoesNotExist(testUserId), exception.getMessage());
    }

}
