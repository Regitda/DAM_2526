package com.backend.service;

import com.backend.dao.*;
import com.backend.dto.score.ScoreDto;
import com.backend.dto.score.UnmarkedScoreItem;
import com.backend.exceptions.ServiceValidationException;
import com.backend.service.validations.StudentCourseExistenceValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentCourseExistenceValidator studentCourseValidator;

    public ScoreService(ScoreRepository scoreRepository, EnrollmentRepository enrollmentRepository, StudentCourseExistenceValidator studentCourseValidator) {
        this.scoreRepository = scoreRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentCourseValidator = studentCourseValidator;
    }


    @Transactional(readOnly = true)
    public List<UnmarkedScoreItem> listUnmarkedScores(String studentId, int courseId) {


        studentCourseValidator.requireStudentAndCourse(studentId, courseId);
        var enrollment = enrollmentRepository.findEnrollmentYearsOrderedDesc(studentId, courseId);
        if (enrollment.isEmpty()) {
            throw new ServiceValidationException("Student is not enrolled in that course");
        }

        var scores = scoreRepository.getUnmarkedScores(enrollment.get(0));
        return scores.stream().map(sc -> new UnmarkedScoreItem(sc.getId(), sc.getSubject().getName())).toList();

    }

    @Transactional
    public ScoreDto setScore(int scoreId, int value) {

        var score = scoreRepository.findById(scoreId);
        if (score.isEmpty()) throw new ServiceValidationException("Score not found");
        var scoreVar = score.get();
        scoreVar.setScore(value);
        return new ScoreDto(scoreVar.getEnrollment().getId(), scoreVar.getSubject().getId(), scoreVar.getScore(), scoreVar.getId());
    }

}
