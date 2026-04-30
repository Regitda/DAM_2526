package org.activity.service;

import org.activity.dao.CourseDao;
import org.activity.dao.EnrollmentDao;
import org.activity.dao.StudentDao;
import org.activity.exceptions.ServiceValidationException;
import org.activity.service.validations.StudentCourseExistenceValidator;
import org.activity.utils.HibernateUtil;

import java.util.List;

public class DisplayService {


    private final CourseDao courseDAO = new CourseDao();
    private final StudentDao studentDAO = new StudentDao();
    private final EnrollmentDao enrollmentDAO = new EnrollmentDao();
    private final StudentCourseExistenceValidator studentCourseValidator = new StudentCourseExistenceValidator(studentDAO, courseDAO);


    public record DisplayDto(Integer year, String subject, Integer mark) {
    }

    public List<DisplayDto> displayAll(String studentId, int courseCode) {

        try (var session = HibernateUtil.getSessionFactory().openSession()) {

            var tx = session.beginTransaction();
            try {
                var studentAndCourse = studentCourseValidator.requireStudentAndCourse(session, studentId, courseCode);

                var student = studentAndCourse.student().getIdcard();
                var course = studentAndCourse.cours().getId();
                var result = studentDAO.getDisplayData(session, student, course);
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            }
        }
    }
}
