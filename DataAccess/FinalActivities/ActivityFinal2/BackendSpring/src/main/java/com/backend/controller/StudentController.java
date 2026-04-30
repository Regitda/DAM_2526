package com.backend.controller;

import com.backend.dto.student.StudentImportDto;
import com.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @PostMapping()
    public void addStudent(@Valid @RequestBody StudentImportDto student) {
        studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public void updateStudent(
            @PathVariable String id,
            @Valid @RequestBody StudentImportDto student
    ) {
        studentService.updateStudent(id, student);
    }

    /* Not asked anymore, so will just keep commented out.
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
    }*/
}
