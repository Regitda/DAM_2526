package com.da.dg2526.controllers;

import com.da.dg2526.cli.HelpPrinter;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.validations.Validation;
import com.da.dg2526.controllers.validations.ValidationMessages;
import com.da.dg2526.controllers.validations.ValidationResult;
import com.da.dg2526.exceptions.ControllerValidationException;
import com.da.dg2526.models.dto.BookDataDto;
import com.da.dg2526.models.dto.BookXmlDto;
import com.da.dg2526.restapi.RestApiConnection;
import com.da.dg2526.utils.LoggerUtil;
import com.da.dg2526.xmlParser.BookXmlParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddNewBook {

    private final RestApiConnection connection;


    public AddNewBook(RestApiConnection conn) {
        connection = conn;
    }

    public void addFromXml(ParsedCommand parsed) {
        var args = parsed.args();

        if (args.length != 1) {
            LoggerUtil.logError(ValidationMessages.argumentsAmountNotCorrect(args.length, 1));
            HelpPrinter.printAddHelp();
            return;
        }

        var xmlVerificationResult = Validation.verifyXmlFilePath(args[0]);

        if (xmlVerificationResult.isInvalid()) {
            var error = new StringBuilder();
            Validation.appendError(error, xmlVerificationResult);
            LoggerUtil.logError(error.toString());
            HelpPrinter.printAddHelp();
            return;
        }

        List<BookXmlDto> rawBooks = new ArrayList<>();

        var xmlPath = xmlVerificationResult.getValueOrThrow();
        try {
            rawBooks = BookXmlParser.parseSAX(xmlPath);
        } catch (Exception e) {
            LoggerUtil.logError(e.getMessage());
            HelpPrinter.printAddHelp();
            return;
        }
        if (rawBooks.isEmpty()) {
            LoggerUtil.logError(ValidationMessages.xmlFileIsEmptyOrBroken());
            return;
        }

        StringBuilder errors = new StringBuilder();
        List<BookDataDto> validBooks = new ArrayList<>();

        for (int index = 0; index < rawBooks.size(); index++) {
            var result = validateBook(rawBooks.get(index), index);
            if (result.isInvalid()) {
                result.error().ifPresent(error -> errors.append(error).append("\n"));
            } else {
                if (result.value().isPresent()) validBooks.add(result.value().get());
            }
        }

        if (!errors.isEmpty()) {
            LoggerUtil.logError(errors.toString());
            return;
        }
        String books;
        try {
            books = booksToJson(validBooks);
        } catch (JSONException e) {
            throw new ControllerValidationException(e.getMessage());
        }

        try {
            connection.post("/books/import", books);
        } catch (IOException e) {
            throw new ControllerValidationException(e.getMessage());
        }
    }

    private ValidationResult<BookDataDto> validateBook(BookXmlDto rawBook, int index) {
        StringBuilder errors = new StringBuilder();

        var isbn = Validation.verifyIsbn(rawBook.isbn());
        var title = Validation.verifyTitle(rawBook.title());
        var copies = Validation.verifyCopies(rawBook.copies());
        var outline = Validation.verifyOutline(rawBook.outline());
        var publisher = Validation.verifyPublisher(rawBook.publisher());
        var category = Validation.verifyCategory(rawBook.category());

        Validation.appendError(errors, isbn);
        Validation.appendError(errors, title);
        Validation.appendError(errors, copies);
        Validation.appendError(errors, outline);
        Validation.appendError(errors, publisher);
        Validation.appendError(errors, category);

        if (!errors.isEmpty()) {
            return ValidationResult.fail("\nBook input at index " + index + " has errors:\n" + errors);
        }

        BookDataDto cleanedBook = new BookDataDto(isbn.getValueOrThrow(), title.getValueOrThrow(), copies.getValueOrThrow(), outline.getValueOrThrow(), publisher.getValueOrThrow(), category.getValueOrThrow());

        return ValidationResult.ok(cleanedBook);
    }

    private String booksToJson(List<BookDataDto> books) throws JSONException {
        var jsonArray = new JSONArray();
        for (BookDataDto book : books) {
            jsonArray.put(toJson(book));
        }
        return jsonArray.toString();

    }

    private JSONObject toJson(BookDataDto books) throws JSONException {
        var json = new JSONObject();
        json.put("isbn", books.isbn());
        json.put("title", books.title());
        json.put("copies", books.copies());
        json.put("outline", books.outline());
        json.put("publisher", books.publisher());
        json.put("category", books.category());
        return json;
    }

}
