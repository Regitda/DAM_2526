package com.backend.dao;


import com.backend.entities.Enrollment;
import com.backend.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {

    @Query("""
        select sc
        from Score sc
        join fetch sc.subject
        where sc.enrollment = :enrollment
          and sc.score is null
    """)
    List<Score> getUnmarkedScores(@Param("enrollment") Enrollment enrollment);
}
