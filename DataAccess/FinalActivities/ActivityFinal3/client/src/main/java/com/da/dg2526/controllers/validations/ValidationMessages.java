package com.da.dg2526.controllers.validations;

import java.nio.file.Path;

public final class ValidationMessages {

    public static String argumentsAmountNotCorrect(Integer amountProvided, Integer amountRequired) {
        return "Number of arguments provided is not correct, provided: " + amountProvided + ", required:" + amountRequired+".";
    }

    public static String userIdRequired() {
        return "User ID is required.";
    }

    public static String userIdTooLong(String userId) {
        return "User ID is too long. Maximum is " + ValidationLimits.MAX_STUDENT_ID_LENGTH + " characters, current length is " + userId.length() + ".";
    }

    public static String bookCodeRequired() {
        return "Book code is required.";
    }

    public static String bookCodeTooLong(String bookCode) {
        return "Book code is too long. Maximum is " + ValidationLimits.MAX_BOOK_CODE_LENGTH + " characters, current length is " + bookCode.length() + ".";
    }

    public static String isbnRequired() {
        return "ISBN is required.";
    }

    public static String isbnTooLong(String isbn) {
        return "ISBN is too long. Maximum is " + ValidationLimits.MAX_ISBN_LENGTH + " characters, current length is " + isbn.length() + ".";
    }

    public static String xmlPathRequired() {
        return "XML file path is required.";
    }

    public static String xmlPathInvalid(String path) {
        return "Invalid file path: " + path;
    }

    public static String xmlFileDoesNotExist(Path path) {
        return "File does not exist: " + path;
    }

    public static String xmlFileIsNotFile(Path path) {
        return "Provided path is not a file: " + path;
    }

    public static String xmlFileIsNotReadable(Path path) {
        return "File is not readable: " + path;
    }

    public static String xmlFileIsNotXmlFile(Path path) {
        return "File must be an XML file ending with .xml: " + path;
    }

    public static String titleRequired() {
        return "Book title is required.";
    }

    public static String titleTooLong(String title) {
        return "Book title is too long. Maximum is " + ValidationLimits.MAX_TITLE_LENGTH + " characters, current length is " + title.length() + ".";
    }

    public static String copiesRequired() {
        return "Book copies are required.";
    }

    public static String copiesNotNumber(String copies) {
        return "Book copies are not number: " + copies;
    }

    public static String copiesNegative(int copies) {
        return "Book copies cannot be negative. Provided value: " + copies + ".";
    }

    public static String outlineTooLong(String outline) {
        return "Book outline is too long. Maximum is " + ValidationLimits.MAX_OUTLINE_LENGTH + " characters, current length is " + outline.length() + ".";
    }

    public static String publisherTooLong(String publisher) {
        return "Book publisher is too long. Maximum is " + ValidationLimits.MAX_PUBLISHER_LENGTH + " characters, current length is " + publisher.length() + ".";
    }

    public static String categoryRequired() {
        return "Category is required.";
    }

    public static String categoryTooLong(String category) {
        return "Category name is too long. Maximum is " + ValidationLimits.MAX_CATEGORY_LENGTH + " characters, current length is " + category.length() + ".";
    }
}