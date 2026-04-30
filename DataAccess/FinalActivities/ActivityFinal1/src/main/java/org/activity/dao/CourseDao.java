package org.activity.dao;

import org.entities.Cours;
import org.entities.Subject;
import org.hibernate.Session;

import java.util.List;

public class CourseDao {

    public Cours findById(Session session, Integer id) {
        return session.find(Cours.class, id);
    }
}
