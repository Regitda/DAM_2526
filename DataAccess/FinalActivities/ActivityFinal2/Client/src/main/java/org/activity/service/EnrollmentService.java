package org.activity.service;

import org.activity.dao.*;
import org.activity.exceptions.ServiceValidationException;
import org.activity.restapi.RestApiConnection;
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

    private final EnrollmentApiManager enrollmentApiManager;

    public EnrollmentService(RestApiConnection connection) {
        enrollmentApiManager = new EnrollmentApiManager(connection);
    }


    public void enrollStudent(String studentId, Integer courseId) {
        try {
            enrollmentApiManager.enrollStudent(studentId, courseId);
        } catch (Exception e) {
            throw new ServiceValidationException(e.getMessage());
        }

    }


}
