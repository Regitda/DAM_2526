package org.activity.dao;

import org.entities.Subject;
import org.hibernate.Session;

import java.util.List;

public class SubjectDao {
    public List<Subject> firstYearSubjects(Session session, int courseCode) {
        return session.createQuery("""
                    select sc.subject
                    from SubjectCours sc
                    where sc.course.id = :courseCode
                      and sc.subject.year = 1
                """, Subject.class)
                .setParameter("courseCode", courseCode)
                .getResultList();
    }

    public List<Subject> secondYearSubjects(Session session, int courseCode) {
        return session.createQuery("""
                    select sc.subject
                    from SubjectCours sc
                    where sc.course.id = :courseCode
                      and sc.subject.year = 2
                """, Subject.class)
                .setParameter("courseCode", courseCode)
                .getResultList();
    }

    public List<Integer> findSubjectIdsNotPassedByStudentAndCourse(Session session, String studentId, int courseCode) {
        return session.createNativeQuery("select subject_id from vtschool.subjects_not_passed_DG_2526(:id, :course)", Integer.class)
                .setParameter("id", studentId)
                .setParameter("course", courseCode)
                .getResultList();
    }

    public Subject getReferenceById(Session session, Integer code) {
        return session.getReference(Subject.class, code); //Get apparently is depreciated
    }

}
