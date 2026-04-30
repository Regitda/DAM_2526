package com.backend.service.validations;

import com.backend.dao.CourseRepository;
import com.backend.dao.StudentRepository;
import com.backend.exceptions.ServiceValidationException;
import com.backend.entities.Cours;
import com.backend.entities.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentCourseExistenceValidator {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentCourseExistenceValidator(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public record StudentCourse(Student student, Cours cours) {} // Custom record just for validateExistence

    public StudentCourse requireStudentAndCourse(String studentId, Integer courseId) {
        StringBuilder errors = new StringBuilder();

        // REQUIRED: Course and Student to actually exist.

        Student student = studentRepository.findById(studentId).orElse(null);
        Cours course = courseRepository.findById(courseId).orElse(null);

        if (course == null)
            errors.append("Course with id:")
                    .append(courseId)
                    .append(" was not found\n");

        if (student == null)
            errors.append("Student with id:")
                    .append(studentId)
                    .append(" was not found\n");

        if (!errors.isEmpty())  throw new ServiceValidationException(errors.toString());

        return new StudentCourse(student, course);
    }

}
