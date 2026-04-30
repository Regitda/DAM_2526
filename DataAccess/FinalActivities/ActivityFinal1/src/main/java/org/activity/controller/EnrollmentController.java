package org.activity.controller;

import org.activity.cli.HelpPrinter;
import org.activity.cli.ParsedCommand;
import org.activity.service.EnrollmentService;
import org.activity.utils.LoggerUtil;
import org.activity.controller.validations.InputValidation;

public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private static final int EXPECTED_ARGS = 2;

    public EnrollmentController() {
        enrollmentService = new EnrollmentService();
    }

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public void enrollStudent(ParsedCommand parsed) {

        var args = parsed.args();

        if (args.length != EXPECTED_ARGS) {
            LoggerUtil.logError("Invalid number of arguments.");
            HelpPrinter.printEnrolHelp();
            return;
        }

        // Check for validity both values and if they are get cleaned up values back.
        var studentIdVerificationResult = InputValidation.verifyStudentID(args[0]);
        var courseIdVerificationResult = InputValidation.verifyCourseCode(args[1]);

        // Error codes
        if (studentIdVerificationResult.isInvalid() || courseIdVerificationResult.isInvalid()) {

            studentIdVerificationResult.error().ifPresent(LoggerUtil::logError);
            courseIdVerificationResult.error().ifPresent(LoggerUtil::logError);

            HelpPrinter.printEnrolHelp();
            return;
        }

        // Clean values passed to service.
        var studentID = studentIdVerificationResult.getValueOrThrow();
        var courseID = courseIdVerificationResult.getValueOrThrow();


        try {
            enrollmentService.enrollStudent(studentID, courseID);
        } catch (RuntimeException e) {
            LoggerUtil.logError("ERROR: Enrollment failed\n");
            LoggerUtil.logError(e.getMessage());
        }
    }
}
