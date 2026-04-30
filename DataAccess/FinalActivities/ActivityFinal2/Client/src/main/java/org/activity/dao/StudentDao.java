package org.activity.dao;

import org.activity.service.DisplayService;
import org.entities.Student;
import org.hibernate.Session;

import java.util.List;

public class StudentDao {

    public boolean existsByIdCard(Session session, String idCard) {
        Integer one = session.createQuery("""
                    select 1
                    from Student s
                    where s.idcard = :id
                """, Integer.class).setParameter("id", idCard).setMaxResults(1).uniqueResult();

        return one != null;
    }

    public void save(Session session, Student entity) {
        session.persist(entity);
    }

    public Student findById(Session session, String idCard) {
        return session.find(Student.class, idCard); //Get apparently is depreciated
    }

    // Purely for console soo.
    public List<DisplayService.DisplayDto> getDisplayData(Session session, String studentId, int courseCode) {
        var rows = session.createNativeQuery("""
                    select *
                    from vtschool.year_subject_and_mark_DG_2526(:student, :course)
                """, Object[].class).setParameter("student", studentId).setParameter("course", courseCode).getResultList();

        return rows.stream().map(r -> new DisplayService.DisplayDto((Integer)
                r[0],   // year
                (String) r[1],   // subject
                (Integer) r[2]   // mark
        )).toList();
    }

}
