package org.activity.service.validations;

import org.activity.dao.CourseDao;
import org.activity.dao.StudentDao;
import org.activity.exceptions.ServiceValidationException;
import org.entities.Cours;
import org.entities.Student;
import org.hibernate.Session;

public class StudentCourseExistenceValidator {

    private final StudentDao studentDao;
    private final CourseDao courseDao;

    public StudentCourseExistenceValidator(StudentDao studentDao, CourseDao courseDao) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
    }

    public record StudentCourse(Student student, Cours cours) {} // Custom record just for validateExistence

    public StudentCourse requireStudentAndCourse(Session session, String studentId, Integer courseId) {
        StringBuilder errors = new StringBuilder();

        // REQUIRED: Course and Student to actually exist.
        var result = new StudentCourse(studentDao.findById(session, studentId), courseDao.findById(session, courseId));

        if (result.cours() == null) errors.append("Course with id:").append(courseId).append(" was not found").append("\n");
        if (result.student() == null) errors.append("Student with id:").append(studentId).append(" was not found").append("\n");

        var error = errors.toString();
        if(!error.isEmpty())  throw new ServiceValidationException(error); // If it has errors it shuts all down.
        return result;
    }

}
