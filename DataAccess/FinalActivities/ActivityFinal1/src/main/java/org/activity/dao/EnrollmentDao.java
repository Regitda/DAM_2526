package org.activity.dao;

import org.entities.Enrollment;
import org.hibernate.Session;

import java.util.List;

public class EnrollmentDao {

    // Check if student was enrolled before
    public List<Integer> findYearsByStudentAndCourse(Session session, String studentId, int courseCode) {
        return session.createQuery("""
                select distinct e.year
                from Enrollment e
                where e.student.idcard = :idcard
                and e.course.id = :courseCode
                order by e.year desc
                """, Integer.class).setParameter("idcard", studentId).setParameter("courseCode", courseCode).getResultList();
    }


    // Search for if student has ever been enrolled in a different course on same year.
    public Integer findCourseIdForStudentInYear(Session session, String studentId, int year) {
        return session.createQuery("""
            select e.course.id
            from Enrollment e
            where e.student.idcard = :idcard
              and e.year = :year
        """, Integer.class)
                .setParameter("idcard", studentId)
                .setParameter("year", year)
                .setMaxResults(1)
                .uniqueResult();
    }

    public void save(Session session, Enrollment entity) {
        session.persist(entity);
    }

    public Enrollment findLatestByStudentAndCourse(Session session, String studentId, int courseCode) {
        return session.createQuery("""
            select e
            from Enrollment e
            where e.student.idcard = :idcard
              and e.course.id = :courseCode
            order by e.year desc
            """, Enrollment.class)
                .setParameter("idcard", studentId)
                .setParameter("courseCode", courseCode)
                .setMaxResults(1)
                .uniqueResultOptional()
                .orElse(null);
    }
}
