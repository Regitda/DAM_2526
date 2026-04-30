package org.activity.controller;

import org.activity.cli.HelpPrinter;
import org.activity.cli.ParsedCommand;
import org.activity.restapi.RestApiConnection;
import org.activity.service.ScoreService;
import org.activity.utils.LoggerUtil;
import org.activity.controller.validations.InputValidation;

import java.security.InvalidParameterException;
import java.util.Scanner;

public class ScoreController {

    private final ScoreService scoreService;
    private static final int EXPECTED_ARGS = 2;

    public ScoreController(RestApiConnection connection) {
        scoreService = new ScoreService(connection);
    }

    public ScoreController(ScoreService scoreService,RestApiConnection connection) {
        this.scoreService = scoreService;
    }

    public void scoreStudent(ParsedCommand parser) {
        String[] args = parser.args();
        if (args == null || args.length != EXPECTED_ARGS) {
            LoggerUtil.logError("Invalid number of arguments.");
            HelpPrinter.printQualifyHelp();
            return;
        }

        // Check for validity both values and if they are get cleaned up values back.
        var studentIdVerificationResult = InputValidation.verifyStudentID(args[0]);
        var courseIdVerificationResult = InputValidation.verifyCourseCode(args[1]);

        // Error codes
        if (studentIdVerificationResult.isInvalid() || courseIdVerificationResult.isInvalid()) {

            studentIdVerificationResult.error().ifPresent(LoggerUtil::logError);
            courseIdVerificationResult.error().ifPresent(LoggerUtil::logError);
            HelpPrinter.printQualifyHelp();
            return;
        }

        // Clean values passed to service.
        var studentID = studentIdVerificationResult.getValueOrThrow();
        var courseID = courseIdVerificationResult.getValueOrThrow();

        try {
            // Get list of unmarked scores Dto (ScoreID,
            var scores = scoreService.listUnmarkedScores(studentID, courseID);
            System.out.println("Unmarked scores found: " + scores.size());

            var scanner = new Scanner(System.in);
            for (var score : scores) {
                while (true) {
                    try {
                        System.out.println("Mark subject: " + score.subjectName() + ":");
                        var in = scanner.nextLine();

                        var verificationResultScore = InputValidation.verifyScore(in);

                        if(verificationResultScore.isInvalid()) {
                            System.out.println(verificationResultScore.error());
                            continue;
                        }

                        var intScore = verificationResultScore.getValueOrThrow();

                        scoreService.setScore(score.scoreId(),intScore);

                        System.out.println("Value: "+intScore +" for subject: "+score.subjectName()+" saved!\n" );

                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Value must be an integer\n");
                    }
                }
            }
        } catch (Exception e) {
            LoggerUtil.logError("Scoring failed\n");
            LoggerUtil.logError(e.getMessage());
        }
    }
}

