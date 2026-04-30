package com.backend.dao;


import com.backend.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {


    @Query("""
                select sc.subject
                from SubjectCours sc
                where sc.course.id = :courseCode
                  and sc.subject.year = 1
            """)
    List<Subject> firstYearSubjects(@Param("courseCode") int courseCode);


    @Query("""
                select sc.subject
                from SubjectCours sc
                where sc.course.id = :courseCode
                  and sc.subject.year = 2
            """)
    List<Subject> secondYearSubjects(@Param("courseCode") int courseCode);


    @Query(value = "select subject_id from vtschool.subjects_not_passed_DG_2526(:id, :course)", nativeQuery = true)
    List<Integer> findSubjectIdsNotPassedByStudentInCourse(@Param("id") String studentId,
                                                           @Param("course") int courseCode);
}
