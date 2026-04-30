package org.activity.controller;

import org.activity.cli.HelpPrinter;
import org.activity.cli.ParsedCommand;
import org.activity.restapi.RestApiConnection;
import org.activity.service.StudentService;
import org.activity.utils.LoggerUtil;
import org.activity.controller.validations.InputValidation;

public class StudentController {

    private final StudentService studentService;
    private static final int EXPECTED_ARGS = 1;

    public StudentController(RestApiConnection connection) {
        this(new StudentService(connection));
    }

    public StudentController(StudentService studentService,RestApiConnection connection) {
        this.studentService = studentService;
    }

    public void addFromXml(ParsedCommand parser) {
        String[] args = parser.args();
        if (args == null || args.length != EXPECTED_ARGS) {
            LoggerUtil.logError("Invalid number of arguments.");
            HelpPrinter.printAddHelp();
            return;
        }

        // Check for validity both values and if they are get cleaned up values back.
        var xmlPathVerificationResult = InputValidation.verifyXmlFilePath(args[0]);

        // Error codes
        if (xmlPathVerificationResult.isInvalid()) {
            xmlPathVerificationResult.error().ifPresent(LoggerUtil::logError);
            HelpPrinter.printQualifyHelp();
            return;
        }

        // Clean values passed to service.
        var xmlPath = xmlPathVerificationResult.getValueOrThrow();
        try {
            int imported = studentService.importStudentsFromXml(xmlPath);
            LoggerUtil.logSuccess("SUCCESS: Imported " + imported + " students.");
        } catch (RuntimeException e) {
            LoggerUtil.logError("ERROR: Import failed\n");
            LoggerUtil.logError(e.getMessage());
        }
    }
}
