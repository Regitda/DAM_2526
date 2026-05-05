package com.da.dg2526.controllers.validations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Validation {

    public static ValidationResult<String> verifyStudentID(String studentIdRAW) {
        if (studentIdRAW == null) {
            return ValidationResult.fail(ValidationMessages.userIdRequired());
        }

        String studentID = studentIdRAW.trim();

        if (studentID.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.userIdRequired());
        }

        if (studentID.length() > ValidationLimits.MAX_STUDENT_ID_LENGTH) {
            return ValidationResult.fail(ValidationMessages.userIdTooLong(studentID));
        }

        return ValidationResult.ok(studentID);
    }

    public static ValidationResult<String> verifyBookCode(String bookCodeRAW) {
        if (bookCodeRAW == null) {
            return ValidationResult.fail(ValidationMessages.bookCodeRequired());
        }

        String bookCode = bookCodeRAW.trim();

        if (bookCode.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.bookCodeRequired());
        }

        if (bookCode.length() > ValidationLimits.MAX_BOOK_CODE_LENGTH) {
            return ValidationResult.fail(ValidationMessages.bookCodeTooLong(bookCode));
        }

        return ValidationResult.ok(bookCode);
    }

    public static ValidationResult<String> verifyIsbn(String isbnRAW) {
        if (isbnRAW == null) {
            return ValidationResult.fail(ValidationMessages.isbnRequired());
        }

        String isbn = isbnRAW.trim();

        if (isbn.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.isbnRequired());
        }

        if (isbn.length() > ValidationLimits.MAX_ISBN_LENGTH) {
            return ValidationResult.fail(ValidationMessages.isbnTooLong(isbn));
        }

        return ValidationResult.ok(isbn);
    }

    public static ValidationResult<String> verifyTitle(String titleRAW) {
        if (titleRAW == null) {
            return ValidationResult.fail(ValidationMessages.titleRequired());
        }

        String title = titleRAW.trim();

        if (title.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.titleRequired());
        }

        if (title.length() > ValidationLimits.MAX_TITLE_LENGTH) {
            return ValidationResult.fail(ValidationMessages.titleTooLong(title));
        }

        return ValidationResult.ok(title);
    }

    public static ValidationResult<Path> verifyXmlFilePath(String xmlFilePathRAW) {
        if (xmlFilePathRAW == null) {
            return ValidationResult.fail(ValidationMessages.xmlPathRequired());
        }

        String raw = xmlFilePathRAW.trim();

        if (raw.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.xmlPathRequired());
        }

        final Path path;

        try {
            path = Paths.get(raw);
        } catch (Exception e) {
            return ValidationResult.fail(ValidationMessages.xmlPathInvalid(raw));
        }

        if (!Files.exists(path)) {
            return ValidationResult.fail(ValidationMessages.xmlFileDoesNotExist(path));
        }

        if (!Files.isRegularFile(path)) {
            return ValidationResult.fail(ValidationMessages.xmlFileIsNotFile(path));
        }

        if (!Files.isReadable(path)) {
            return ValidationResult.fail(ValidationMessages.xmlFileIsNotReadable(path));
        }

        if (!path.getFileName().toString().toLowerCase().endsWith(".xml")) {
            return ValidationResult.fail(ValidationMessages.xmlFileIsNotXmlFile(path));
        }

        return ValidationResult.ok(path);
    }

    public static ValidationResult<Integer> verifyCopies(String copiesRAW) {
        if (copiesRAW == null) {
            return ValidationResult.fail(ValidationMessages.copiesRequired());
        }

        String raw = copiesRAW.trim();

        if (raw.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.copiesRequired());
        }

        final int copies;

        try {
            copies = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return ValidationResult.fail(ValidationMessages.copiesNotNumber(copiesRAW));
        }

        if (copies < 0) {
            return ValidationResult.fail(ValidationMessages.copiesNegative(copies));
        }

        return ValidationResult.ok(copies);
    }

    public static ValidationResult<String> verifyOutline(String outlineRAW) {
        if (outlineRAW == null) {
            return ValidationResult.ok("");
        }

        String outline = outlineRAW.trim();

        if (outline.length() > ValidationLimits.MAX_OUTLINE_LENGTH) {
            return ValidationResult.fail(ValidationMessages.outlineTooLong(outline));
        }

        return ValidationResult.ok(outline);
    }

    public static ValidationResult<String> verifyPublisher(String publisherRAW) {
        if (publisherRAW == null) {
            return ValidationResult.ok("");
        }

        String publisher = publisherRAW.trim();

        if (publisher.length() > ValidationLimits.MAX_PUBLISHER_LENGTH) {
            return ValidationResult.fail(ValidationMessages.publisherTooLong(publisher));
        }

        return ValidationResult.ok(publisher);
    }

    public static ValidationResult<String> verifyCategory(String categoryRAW) {
        if (categoryRAW == null) {
            return ValidationResult.fail(ValidationMessages.categoryRequired());
        }

        String category = categoryRAW.trim();

        if (category.isEmpty()) {
            return ValidationResult.fail(ValidationMessages.categoryRequired());
        }

        if (category.length() > ValidationLimits.MAX_CATEGORY_LENGTH) {
            return ValidationResult.fail(ValidationMessages.categoryTooLong(category));
        }

        return ValidationResult.ok(category);
    }
}