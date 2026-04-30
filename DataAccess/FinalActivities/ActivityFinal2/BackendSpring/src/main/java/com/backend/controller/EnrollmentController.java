package com.backend.controller;

import com.backend.dto.enrollment.ReturnEnrollmentDto;
import com.backend.service.EnrollmentService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping()
    public ReturnEnrollmentDto enrollStudent(@RequestParam @NotBlank @Size(max = 12) String studentId, @RequestParam @Min(1) int courseId) {
       return enrollmentService.enrollStudent(studentId, courseId);
    }
}
