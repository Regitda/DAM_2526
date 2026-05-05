package com.da.dg2526.controllers;

import com.da.dg2526.cli.HelpPrinter;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.validations.Validation;
import com.da.dg2526.controllers.validations.ValidationMessages;
import com.da.dg2526.controllers.validations.ValidationResult;
import com.da.dg2526.exceptions.ControllerValidationException;
import com.da.dg2526.models.dto.BookInputDto;
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
            xmlVerificationResult.error().ifPresent(LoggerUtil::logError);
            HelpPrinter.printAddHelp();
            return;
        }

        var xmlPath = xmlVerificationResult.getValueOrThrow();
        var rawBooks = BookXmlParser.parse(xmlPath);

        StringBuilder errors = new StringBuilder();
        List<BookInputDto> validBooks = new ArrayList<>();

        for (int index = 0; index < rawBooks.size(); index++) {
            var result = validateBook(rawBooks.get(index), index);
            if (result.isInvalid()) {
                result.error().ifPresent(error -> errors.append(error).append("\n"));
            } else {
                if (result.value().isPresent()) validBooks.add(result.value().get());
            }
        }

        if (!errors.isEmpty()) {
            throw new ControllerValidationException(errors.toString());
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

    private ValidationResult<BookInputDto> validateBook(BookXmlDto rawBook, int index) {
        StringBuilder errors = new StringBuilder();

        var isbn = Validation.verifyIsbn(rawBook.isbn());
        var title = Validation.verifyTitle(rawBook.title());
        var copies = Validation.verifyCopies(rawBook.copies());
        var outline = Validation.verifyOutline(rawBook.outline());
        var publisher = Validation.verifyPublisher(rawBook.publisher());
        var category = Validation.verifyCategory(rawBook.category());

        appendError(errors, isbn);
        appendError(errors, title);
        appendError(errors, copies);
        appendError(errors, outline);
        appendError(errors, publisher);
        appendError(errors, category);

        if (!errors.isEmpty()) {
            return ValidationResult.fail("Book input at index " + index + " has errors:\n" + errors);
        }

        BookInputDto cleanedBook = new BookInputDto(isbn.getValueOrThrow(), title.getValueOrThrow(), copies.getValueOrThrow(), outline.getValueOrThrow(), publisher.getValueOrThrow(), category.getValueOrThrow());

        return ValidationResult.ok(cleanedBook);
    }

    private static <T> void appendError(StringBuilder errors, ValidationResult<T> result) {
        result.error().ifPresent(error -> errors.append("- ").append(error).append("\n"));
    }

    private String booksToJson(List<BookInputDto> books) throws JSONException {
        var jsonArray = new JSONArray();
        for (BookInputDto book : books) {
            jsonArray.put(toJson(book));
        }
        return jsonArray.toString();

    }

    private JSONObject toJson(BookInputDto books) throws JSONException {
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
