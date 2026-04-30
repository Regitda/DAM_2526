package org.activity.service;

import org.activity.dao.*;
import org.activity.exceptions.ServiceValidationException;
import org.activity.service.validations.StudentCourseExistenceValidator;
import org.activity.utils.HibernateUtil;
import org.activity.utils.LoggerUtil;
import org.entities.Cours;
import org.entities.Enrollment;
import org.entities.Student;
import org.entities.Subject;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EnrollmentService {

    private final EnrollmentDao enrollmentDAO = new EnrollmentDao();
    private final StudentDao studentDAO = new StudentDao();
    private final CourseDao courseDAO = new CourseDao();
    private final ScoreDao scoreDAO = new ScoreDao();
    private final SubjectDao subjectDAO = new SubjectDao();
    private final StudentCourseExistenceValidator studentCourseValidator = new StudentCourseExistenceValidator(studentDAO, courseDAO);


    public void enrollStudent(String studentId, Integer courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                var validationResult = studentCourseValidator.requireStudentAndCourse(session, studentId, courseId);

                // Searches if there was enrolment of student in a course.
                var enrollmentYears = enrollmentDAO.findYearsByStudentAndCourse(session, studentId, courseId);

                // Get not passed signatures.
                Set<Subject> notPassed = failedSignatures(session, studentId, courseId, enrollmentYears); // Empty if its new year.


                // Get next enroll year
                var nextEnrollYear = nextEnrollYear(enrollmentYears, session, studentId, courseId);


                // Enroll the student, so far without scores.
                Enrollment enrollment = toEntity(validationResult.cours(), validationResult.student(), nextEnrollYear);
                enrollmentDAO.save(session, enrollment);

                var subjects = insertEmptyScoresInSubjects(session, studentId, courseId, enrollment, notPassed, enrollmentYears);

                tx.commit();
                LoggerUtil.logSuccess("Enrollment " + studentId + " enrolled in course " + courseId + " in year " + nextEnrollYear);
                String names = subjects.stream().map(Subject::getName).toList().toString();
                System.out.println(studentId + " enrolled in subjects " + names);
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    private Set<Subject> insertEmptyScoresInSubjects(Session session, String studentId, Integer courseId, Enrollment enrollment, Set<Subject> notPassed, List<Integer> enrollmentYears) {
        Set<Subject> subjectsToScore = new HashSet<>();

        //Get correct subjects to add to scores (Non-passed or new)
        if (enrollmentYears.isEmpty()) {
            subjectsToScore.addAll(subjectDAO.firstYearSubjects(session, courseId));
        } else if ((enrollmentYears.size() == 1)) { // Checks if student is going for second year.
            subjectsToScore.addAll(subjectDAO.secondYearSubjects(session, courseId));
        }

        subjectsToScore.addAll(notPassed);

        //Insert empty scores.
        scoreDAO.insertEmptyScores(session, enrollment, subjectsToScore);
        return subjectsToScore;
    }

    private Set<Subject> failedSignatures(Session session, String studentId, Integer courseId, List<Integer> enrollmentYears) {
        if (enrollmentYears.isEmpty()) { // First year, could not have failed.
            return Set.of();
        }
        var ids = subjectDAO.findSubjectIdsNotPassedByStudentAndCourse(session, studentId, courseId);
        if (ids.isEmpty()) {
            throw new ServiceValidationException("Student " + studentId + " already finished course " + courseId);
        }

        Set<Subject> notPassed = new HashSet<>();
        for (Integer id : ids) {
            notPassed.add(subjectDAO.getReferenceById(session, id));
        }
        return notPassed;
    }


    private Integer nextEnrollYear(List<Integer> enrollmentYears, Session session, String idCard, int courseCode) {
        //Gets the next year for enrollment depending on what was last and current year.
        int currentYear = java.time.Year.now().getValue();
        int nextPossibleYear = (enrollmentYears.isEmpty()) ? currentYear : enrollmentYears.get(0) + 1;
        int nextEnrollYear = Math.max(currentYear, nextPossibleYear);

        // Making sure student is not enrolled on a different course on same year.
        boolean enrolledInDifferentCourseSameYear = enrollmentDAO.findCourseIdForStudentInYear(session, idCard, nextEnrollYear) != null;

        if (enrolledInDifferentCourseSameYear) { // Throws error if already enrolled in a different course.
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
