package org.activity.controller.validations;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputValidation {


    private static final int MAX_STUDENT_ID_LEN = 12;

    public static ValidationResult<String> verifyStudentID(String studentIdRAW) {
        if (studentIdRAW == null) {
            return ValidationResult.fail("Student ID is empty");
        }

        String studentID = studentIdRAW.trim();

        if (studentID.isEmpty()) {
            return ValidationResult.fail("Student ID is empty");
        }

        if (studentID.length() > MAX_STUDENT_ID_LEN) {
            return ValidationResult.fail("Student ID is too long");
        }

        return ValidationResult.ok(studentID);
    }


    public static ValidationResult<Integer> verifyCourseCode(String courseCodeRAW) {
        if (courseCodeRAW == null) {
            return ValidationResult.fail("Student ID is empty.");
        }

        String studentID = courseCodeRAW.trim();

        if (studentID.isEmpty()) {
            return ValidationResult.fail("Student ID is empty.");
        }

        int courseCode;
        try {
            courseCode = Integer.parseInt(studentID);
        } catch (NumberFormatException e) {
            return ValidationResult.fail("Invalid course code.");
        }
        return ValidationResult.ok(courseCode);
    }

    public static ValidationResult<Path> verifyXmlFilePath(String xmlFilePathRAW) {
        if (xmlFilePathRAW == null) {
            return ValidationResult.fail("XML file path is empty.");
        }

        String raw = xmlFilePathRAW.trim();
        if (raw.isEmpty()) {
            return ValidationResult.fail("XML file path is empty.");
        }

        final Path path;
        try {
            path = Paths.get(raw);
        } catch (Exception e) {
            return ValidationResult.fail("Invalid file path: " + raw);
        }

        if (!Files.exists(path)) {
            return ValidationResult.fail("File does not exist: " + path);
        }
        if (!Files.isRegularFile(path)) {
            return ValidationResult.fail("Path is not a file: " + path);
        }
        if (!Files.isReadable(path)) {
            return ValidationResult.fail("File is not readable: " + path);
        }


        // In case it's not xml file.
        if (!path.getFileName().toString().toLowerCase().endsWith(".xml")) {
            return ValidationResult.fail("File is not an XML (.xml): " + path);
        }

        return ValidationResult.ok(path);
    }


    public static ValidationResult<Integer> verifyScore(String scoreRAW) {
        String raw = scoreRAW.trim();
        if (raw.isEmpty())
            return ValidationResult.fail("Score is empty.");


        int intValue;
        try {
            intValue = Integer.parseInt(scoreRAW);
        } catch (NumberFormatException e) {
            return ValidationResult.fail("Score is not a number.");
        }

        if(intValue == 99) return ValidationResult.fail("Score skipped due to exit value 99.");

        if (intValue < 0 || intValue > 10)
            return ValidationResult.fail("Score " + intValue + " is out of range (0-10).");

        return ValidationResult.ok(intValue);

    }

}
