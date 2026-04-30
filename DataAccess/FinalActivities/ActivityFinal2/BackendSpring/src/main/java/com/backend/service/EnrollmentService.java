package com.backend.service;

import com.backend.dao.*;
import com.backend.dto.enrollment.ReturnEnrollmentDto;
import com.backend.entities.*;
import com.backend.exceptions.ServiceValidationException;
import com.backend.service.validations.StudentCourseExistenceValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final ScoreRepository scoreRepository;
    private final SubjectRepository subjectRepository;
    private final StudentCourseExistenceValidator studentCourseValidator;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, ScoreRepository scoreRepository, SubjectRepository subjectRepository, StudentCourseExistenceValidator studentCourseValidator) {

        this.enrollmentRepository = enrollmentRepository;
        this.scoreRepository = scoreRepository;
        this.subjectRepository = subjectRepository;
        this.studentCourseValidator = studentCourseValidator;
    }


    @Transactional
    public ReturnEnrollmentDto enrollStudent(String studentId, Integer courseId) {

        var validationResult = studentCourseValidator.requireStudentAndCourse(studentId, courseId);

        // Searches if there was enrolment of student in a course.
        var enrollmentYears = enrollmentRepository.findEnrollmentYearsForStudentInCourse(studentId, courseId);

        // Get not passed signatures.
        Set<Subject> notPassed = failedSignatures(studentId, courseId, enrollmentYears); // Empty if its new year.


        // Get next enroll year
        var nextEnrollYear = nextEnrollYear(enrollmentYears, studentId, courseId);


        // Enroll the student, so far without scores.
        Enrollment enrollment = toEntity(validationResult.cours(), validationResult.student(), nextEnrollYear);
        enrollmentRepository.save(enrollment);

        var subjects = insertNewEmtpyScoresIntoDB(courseId, enrollment, notPassed, enrollmentYears);

        // Get subject names for displaying results.
        var names = subjects.stream().map(Subject::getName).toList();

        System.out.println(studentId + " enrolled in subjects " + names);
        return new ReturnEnrollmentDto(studentId,enrollment.getId(),names);

    }

    private Set<Subject> insertNewEmtpyScoresIntoDB(Integer courseId, Enrollment enrollment, Set<Subject> notPassed, List<Integer> enrollmentYears) {
        Set<Subject> subjectsToScore = new HashSet<>();

        //Get correct subjects to add to scores (Non-passed or new)
        if (enrollmentYears.isEmpty()) {
            subjectsToScore.addAll(subjectRepository.firstYearSubjects(courseId));
        } else if ((enrollmentYears.size() == 1)) { // Checks if student is going for second year.
            subjectsToScore.addAll(subjectRepository.secondYearSubjects(courseId));
        }

        subjectsToScore.addAll(notPassed);

        //Insert empty scores.
        for (Subject subject : subjectsToScore) {
            var score = new Score();
            score.setEnrollment(enrollment);
            score.setSubject(subject);
            scoreRepository.save(score);
        }

        return subjectsToScore;
    }

    private Set<Subject> failedSignatures(String studentId, Integer courseId, List<Integer> enrollmentYears) {
        if (enrollmentYears.isEmpty()) { // First year, could not have failed.
            return Set.of();
        }
        var ids = subjectRepository.findSubjectIdsNotPassedByStudentInCourse(studentId, courseId);
        if (ids.isEmpty()) {
            throw new ServiceValidationException("Student " + studentId + " already finished course " + courseId);
        }

        Set<Subject> notPassed = new HashSet<>();
        for (Integer id : ids) {
            notPassed.add(subjectRepository.getReferenceById(id));
        }
        return notPassed;
    }


    private Integer nextEnrollYear(List<Integer> enrollmentYears, String idCard, int courseCode) {
        //Gets the next year for enrollment depending on what was last and current year.
        int currentYear = java.time.Year.now().getValue();
        int nextPossibleYear = (enrollmentYears.isEmpty()) ? currentYear : enrollmentYears.get(0) + 1;
        int nextEnrollYear = Math.max(currentYear, nextPossibleYear);

        // Making sure student is not enrolled on a different course on same year.
        boolean notEnrolledInDifferentCourseInSameYear = enrollmentRepository.findCourseIdsForStudentInYear(idCard, nextEnrollYear).isEmpty();

        if (!notEnrolledInDifferentCourseInSameYear) { // Throws error if already enrolled in a different course.
            throw new ServiceValidationException("Student " + idCard + " already enrolled for year " + nextEnrollYear + " in course " + courseCode);
        }
        return nextEnrollYear;
    }


    private Enrollment toEntity(Cours course, Student student, Integer year) {
        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        e.setYear(year);
        return e;
    }

}
