package com.da.dg2526.api.services;

import com.da.dg2526.api.models.dto.bookEntity.BookNewInputDTO;
import com.da.dg2526.api.models.entities.BookEntity;

import java.time.LocalDate;

public class ServiceErrorMessages {


    // Book errors //
    public static String bookCategoryDoesNotExist(BookNewInputDTO bookNewInputDTO) {
        return "Book with ISBN: " + bookNewInputDTO.isbn() + " category doesn't exist: " + bookNewInputDTO.category() + "\n";
    }

    public static String bookAlreadyExists(BookNewInputDTO bookNewInputDTO) {
        return "Book already exists: " + bookNewInputDTO.isbn() + "\n";
    }

    public static String bookImportFullyFailed(Integer size, StringBuilder errors) {
        return "Imported books all failed, " + size + " books were invalid. Failed with these errors:\n" + errors;
    }

    public static String bookReturnedSuccess(BookEntity book, boolean fined) {
        if (fined) return "Book " + book.getTitle() + " was returned. Because of late return user was fined.";
        return "Book " + book.getTitle() + " was returned. The book was returned in time.";
    }

    public static String lendingNotFound(String isbn, String userId) {
        return "Lending of book with ISBN: " + isbn + " owned by user: " + userId + " was not found.";
    }


    public static String bookHasFreeLendings(String isbn, Integer amount) {
        return "Book with ISBN " + isbn + " has " + amount + " free copies to lend";
    }

    public static String bookHasNoFreeLendings(String isbn, Integer copiesAmount, Integer lendings) {
        return "All books have been lent: Book with ISBN: " + isbn + " has " + copiesAmount + " copies, out of which are lent: " + lendings;
    }

    public static String bookDoesNotExist(String isbn) {
        return "Book with ISBN: " + isbn + " does not exist.\n";
    }


    // User errors
    public static String userCurrentlyFined(String userId, LocalDate date) {
        return "User " + userId + " is currently fined, end of fine: " + date;
    }

    public static String userPersonalDetailsMissing(String userId) {
        return "User with ID: " + userId + " lacks phone/email.";
    }

    public static String userIsFined(String userId, LocalDate date) {
        return "User " + userId + " is currently fined, end of fine: " + date;
    }

    public static String userLendingLimitExceeded(String userId, Integer amount) {
        return "User with " + userId + " currently is borrowing " + amount + " books. Limit is 3";
    }

    public static String userDoesNotExist(String userId) {
        return "User with ID: " + userId + " does not exist.\n";
    }

    // Reserve errors
    public static String reserveRequestUserNotOwner(String isbn) {
        return "The book with ISBN " + isbn + " is currently reserved by a different user. Book is reservable only.";
    }

    public static final String bookAddErrorGeneralMessage = "Book creation failed with these errors:\n";
    public static final String bookImportGeneralMessageError = "Book import partially failed with these errors:\n";
}
