package com.backend.dto.enrollment;

import java.util.List;

public record ReturnEnrollmentDto(String studentId, Integer enrollmentId, List<String> subjectNames) {
}
