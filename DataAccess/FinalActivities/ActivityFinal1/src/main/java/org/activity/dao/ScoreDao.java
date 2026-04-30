package org.activity.dao;

import org.activity.utils.HibernateUtil;
import org.entities.Enrollment;
import org.entities.Score;
import org.entities.Subject;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreDao {

    public void insertEmptyScores(Session session, Enrollment enrollment, Set<Subject> subjects) {
        Set<Subject> uniqueSubjects = new HashSet<>(subjects);
        for (Subject subject : uniqueSubjects) {
            session.persist(new Score(enrollment, subject));
        }
    }

    public void newScore(Session session, Enrollment enrollment, Subject subject, Integer scoreNum) {
        session.persist(new Score(enrollment, subject, scoreNum));
    }


    public List<Score> getUnmarkedScores(Session session, Enrollment enrollment) {
        return session.createQuery("""
        select sc
        from Score sc
        join fetch sc.subject
        where sc.enrollment = :enrollment
          and sc.score is null
    """, Score.class)
                .setParameter("enrollment", enrollment)
                .getResultList();
    }

    public void saveScore(Session session, Score score) {
        session.persist(score);
    }

    public Score findById(Session session, Integer scoreCode) {
       return session.find(Score.class, scoreCode);
    }
}
