package com.da.dg2526.controllers;

import com.da.dg2526.cli.HelpPrinter;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.validations.Validation;
import com.da.dg2526.controllers.validations.ValidationMessages;
import com.da.dg2526.restapi.RestApiConnection;
import com.da.dg2526.utils.LoggerUtil;
import org.json.JSONException;

import java.io.IOException;

public class ReturnBook {

    private final RestApiConnection connection;

    public ReturnBook(RestApiConnection conn) {
        this.connection = conn;
    }
    public void returnBook(ParsedCommand parsed) {

        var args = parsed.args();

        if (args.length != 2) {
            LoggerUtil.logError(ValidationMessages.argumentsAmountNotCorrect(args.length, 2));
            HelpPrinter.printReturnHelp();
            return;
        }

        var result = Validation.validateStudentAndBook(args[0], args[1]);

        if (!result.errors().isEmpty()) {
            LoggerUtil.logError(result.errors());
            HelpPrinter.printLendHelp();
            return;
        }


        String endpoint = "/books/" + result.isbn() + "/return?userId=" + result.userId();
        try {
            var response = connection.post(endpoint, "");

            if (response.getStatusCode() != 200) {
                LoggerUtil.logError("Failed to return the book. Status: " + response.getStatusCode() + " " + response.getStatusMessage() + ". Body: " + response.getBody());
                return;
            }
            LoggerUtil.logInfo("Successfully returned the book.\n" + response.getBody());
        } catch (IOException e) {
            LoggerUtil.logError("Could not connect to server: " + e.getMessage());
        }
    }

}
