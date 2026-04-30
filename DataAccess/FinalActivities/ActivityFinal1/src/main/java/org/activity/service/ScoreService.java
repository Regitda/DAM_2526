package org.activity.service;

import org.activity.dao.*;
import org.activity.dto.score.UnmarkedScoreItem;
import org.activity.exceptions.ServiceValidationException;
import org.activity.service.validations.StudentCourseExistenceValidator;
import org.activity.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Scanner;

public class ScoreService {


    private final ScoreDao scoreDAO = new ScoreDao();
    private final EnrollmentDao enrollmentDAO = new EnrollmentDao();
    private final SubjectDao subjectDAO = new SubjectDao();
    private final StudentDao studentDAO = new StudentDao();
    private final CourseDao courseDAO = new CourseDao();
    private final StudentCourseExistenceValidator studentCourseValidator = new StudentCourseExistenceValidator(studentDAO, courseDAO);

    public List<UnmarkedScoreItem> listUnmarkedScores(String studentId, int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                studentCourseValidator.requireStudentAndCourse(session, studentId, courseId);

                var enrollment = enrollmentDAO.findLatestByStudentAndCourse(session, studentId, courseId);
                if (enrollment == null) {
                    throw new ServiceValidationException("Student is not enrolled in that course");
                }

                var scores = scoreDAO.getUnmarkedScores(session, enrollment);
                var items = scores.stream()
                        .map(sc -> new UnmarkedScoreItem(
                                sc.getId(),
                                sc.getSubject().getName()
                        ))
                        .toList();

                tx.commit();
                return items;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

    public void setScore(int scoreId, int value) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                var score = scoreDAO.findById(session, scoreId);
                if (score == null) throw new ServiceValidationException("Score not found");

                if (score.getScore() != null) throw new ServiceValidationException("Score already marked");

                score.setScore(value);

                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }

}
