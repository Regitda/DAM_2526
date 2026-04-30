package org.activity;

import org.activity.cli.CommandParser;
import org.activity.cli.HelpPrinter;
import org.activity.cli.ParsedCommand;
import org.activity.controller.DisplayController;
import org.activity.controller.EnrollmentController;
import org.activity.controller.ScoreController;
import org.activity.controller.StudentController;
import org.activity.service.StudentService;
import org.activity.utils.HibernateUtil;
import org.activity.utils.LoggerUtil;
import org.hibernate.Session;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ParsedCommand parsed = CommandParser.parse(args);

        switch (parsed.command()) {
            case HELP:
                HelpPrinter.printFullHelp();
                break;

            case ADD:
                var StudentController = new StudentController();
                StudentController.addFromXml(parsed);
                break;

            case ENROLL:
                var enrollmentController = new EnrollmentController();
                enrollmentController.enrollStudent(parsed);
                break;

            case QUALIFY:
                var scoreController = new ScoreController();
                scoreController.scoreStudent(parsed);
                break;

            case PRINT:
                var displayController = new DisplayController();
                displayController.displayData(parsed);
                break;

            default:
                System.out.println("Unknown command");
                HelpPrinter.printHelp();
        }
    }
}