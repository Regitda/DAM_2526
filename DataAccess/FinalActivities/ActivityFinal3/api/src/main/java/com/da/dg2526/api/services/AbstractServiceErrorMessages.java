package com.da.dg2526.api.services;

import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.models.entities.BookEntity;

import java.time.LocalDate;

public abstract class AbstractServiceErrorMessages {


    // Book errors //
    protected String createErrorBookCategoryDoesNotExistMessage(BookNewInputDTO bookNewInputDTO) {
        return "Book with ISBN: " + bookNewInputDTO.isbn() + " category doesn't exist: " + bookNewInputDTO.category() + "\n";
    }

    protected String createErrorBookAlreadyExistsMessage(BookNewInputDTO bookNewInputDTO) {
        return "Book already exists: " + bookNewInputDTO.isbn() + "\n";
    }

    protected String createErrorBookImportFullyFailedMessage(Integer size, StringBuilder errors) {
        return "Imported books all failed, " + size + " books were invalid. Failed with these errors:\n" + errors;
    }

    protected String createSuccessBookReturnedMessage(BookEntity book, boolean fined) {
        if (fined) return "Book " + book.getTitle() + " was returned. Because of late return user was fined.";
        return "Book " + book.getTitle() + " was returned. The book was returned in time.";
    }

    protected String createErrorLendingNotFoundMessage(String isbn, String userId) {
        return "Lending of book with ISBN: " + isbn + " owned by user: " + userId + " was not found.";
    }


    protected String createErrorBookHasFreeLendings(String isbn, Integer amount) {
        return "Book with ISBN " + isbn + " has " + amount + " free copies to lend";
    }

    protected String createErrorBookHasNoFreeLendings(String isbn, Integer copiesAmount, Integer lendings) {
        return "All books have been lent: Book with ISBN: " + isbn + " has " + copiesAmount + " copies, out of which are lent: " + lendings;
    }

    protected String createErrorBookDoesNotExist(String isbn) {
        return "Book with ISBN: " + isbn + " does not exist.\n";
    }


    // User errors
    protected String createErrorUserCurrentlyFined(String userId, LocalDate date) {
        return "User " + userId + " is currently fined, end of fine: " + date;
    }

    protected String createErrorUserPersonalDetailsMissing(String userId) {
        return "User with ID: " + userId + " lacks phone/email.";
    }

    protected String createErrorUserIsFined(String userId, LocalDate date) {
        return "User " + userId + " is currently fined, end of fine: " + date;
    }

    protected String createErrorUserLendingLimitExceeded(String userId, Integer amount) {
        return "User with " + userId + " currently is borrowing " + amount + " books. Limit is 3";
    }

    protected String createErrorUserDoesNotExist(String userId) {
        return "User with ID: " + userId + " does not exist.\n";
    }

    // Reserve errors
    protected String createErrorReserveRequestUserNotOwner(String isbn) {
        return "The book with ISBN " + isbn + " is currently reserved by a different user. Book is reservable only.";
    }

    protected final String bookAddErrorGeneralMessage = "Book creation failed with these errors:\n";
    protected final String bookImportGeneralMessageError = "Book import partially failed with these errors:\n";
}
