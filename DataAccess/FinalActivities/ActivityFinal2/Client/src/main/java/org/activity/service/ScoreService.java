package org.activity.service;

import org.activity.dao.*;
import org.activity.dto.score.UnmarkedScoreItem;
import org.activity.exceptions.ServiceValidationException;
import org.activity.restapi.RestApiConnection;
import org.activity.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ScoreService {


    private final ScoreApiManager scoreApiManager;

    public ScoreService(RestApiConnection connection) {
        scoreApiManager=new ScoreApiManager(connection);
    }

    public List<UnmarkedScoreItem> listUnmarkedScores(String studentId, int courseId) {

        var scores = scoreApiManager.getUnmarkedScores(studentId,courseId);
        return scores.stream()
                .map(sc -> new UnmarkedScoreItem(
                        sc.getId(),
                        sc.getSubject().getName()
                ))
                .toList();
    }

    public void setScore(int scoreId, int value) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                var score = scoreApiManager.findById(session, scoreId);
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
