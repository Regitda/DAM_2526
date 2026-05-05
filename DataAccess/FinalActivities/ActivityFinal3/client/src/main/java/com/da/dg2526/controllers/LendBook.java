package com.da.dg2526.controllers;

import com.da.dg2526.cli.HelpPrinter;
import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.controllers.validations.Validation;
import com.da.dg2526.controllers.validations.ValidationMessages;
import com.da.dg2526.restapi.RestApiConnection;
import com.da.dg2526.utils.LoggerUtil;
import org.json.JSONException;

import java.io.IOException;

public class LendBook {
    private final RestApiConnection connection;

    public LendBook(RestApiConnection conn) {
        this.connection = conn;
    }

    public void lendBook(ParsedCommand parsed) {

        var args = parsed.args();

        if (args.length != 2) {
            LoggerUtil.logError(ValidationMessages.argumentsAmountNotCorrect(args.length, 2));
            HelpPrinter.printLendHelp();
            return;
        }

        var result = Validation.validateStudentAndBook(args[0], args[1]);

        if (!result.errors().isEmpty()) {
            LoggerUtil.logError(result.errors());
            HelpPrinter.printLendHelp();
            return;
        }


        String endpoint = "/books/" + result.isbn() + "/lend?userId=" + result.userId();
        try {
            var response = connection.post(endpoint, "");

            if (response.getStatusCode() != 200) {
                LoggerUtil.logError("Failed to lend book. Status: " + response.getStatusCode() + " " + response.getStatusMessage() + ". Body: " + response.getBody());
                return;
            }
            LoggerUtil.logInfo("Successfully lend book.\n" + response.getBody());
        } catch (IOException e) {
            LoggerUtil.logError("Could not connect to server: " + e.getMessage());
        }
    }
}
