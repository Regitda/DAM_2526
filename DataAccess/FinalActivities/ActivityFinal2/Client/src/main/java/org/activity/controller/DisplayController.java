package org.activity.controller;

import org.activity.cli.HelpPrinter;
import org.activity.cli.ParsedCommand;
import org.activity.controller.validations.InputValidation;
import org.activity.restapi.RestApiConnection;
import org.activity.service.DisplayService;
import org.activity.service.EnrollmentService;
import org.activity.utils.LoggerUtil;

import java.util.List;

public class DisplayController {

    private final DisplayService displayService;
    private final int EXPECTED_ARGS = 2;


    public DisplayController(RestApiConnection connection) {
        displayService = new DisplayService(connection);
    }

    public DisplayController(DisplayService displayService,RestApiConnection connection) {
        this.displayService = displayService;
    }


    public void displayData(ParsedCommand parsed) {
        var args = parsed.args();

        if (args.length != EXPECTED_ARGS) {
            LoggerUtil.logError("Invalid number of arguments.");
            HelpPrinter.printPrintHelp();
            return;
        }

        var studentVerificationResult = InputValidation.verifyStudentID(args[0]);
        var courseValidationResult = InputValidation.verifyCourseCode(args[1]);

        if (courseValidationResult.isInvalid() || studentVerificationResult.isInvalid()) {

            courseValidationResult.error().ifPresent(LoggerUtil::logError);
            studentVerificationResult.error().ifPresent(LoggerUtil::logError);
            HelpPrinter.printPrintHelp();
            return;
        }

        var studentId = studentVerificationResult.getValueOrThrow();
        var courseCode = courseValidationResult.getValueOrThrow();

        try {
            var display = displayService.displayAll(studentId, courseCode);
            printScoresTable(display);
        } catch (Exception e) {
            LoggerUtil.logError("ERROR: Display failed\n");
            LoggerUtil.logError(e.getMessage());
        }
    }

    public static void printScoresTable(List<DisplayService.DisplayDto> rows) {

        int wYear = 6;
        int wSubject = 28;
        int wScore = 5;

        // Header
        System.out.printf("%-" + wYear + "s  %-" + wSubject + "s  %" + wScore + "s%n",
                "Year", "Subjects", "Score");

        // Divider
        System.out.printf("%s--%s--%s%n",
                "-".repeat(wYear),
                "-".repeat(wSubject),
                "-".repeat(wScore));

        // Rows
        for (var r : rows) {
            String scoreStr = (r.mark() == null) ? "" : r.mark().toString();

            System.out.printf("%-" + wYear + "d  %-" + wSubject + "s  %" + wScore + "s%n",
                    r.year(),
                    truncate(r.subject(), wSubject),
                    scoreStr);
        }
    }


    private static String truncate(String s, int max) {
        if (s == null) return "";
        return (s.length() <= max) ? s : s.substring(0, max - 1) + "…";
    }

}
