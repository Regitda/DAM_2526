package com.backend.dao;

import com.backend.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    // Check if student was enrolled before
    @Query("""
            select distinct e.year
            from Enrollment e
            where e.student.idcard = :idcard
            and e.course.id = :courseCode
            order by e.year desc
            """)
    List<Integer> findEnrollmentYearsForStudentInCourse(@Param("idcard") String studentId, @Param("courseCode") int courseCode);


    // Search for if student has ever been enrolled in a different course on same year.
    @Query("""
                select e.course.id
                from Enrollment e
                where e.student.idcard = :idcard
                  and e.year = :year
                order by e.course.id
            """)
    List<Integer> findCourseIdsForStudentInYear(@Param("idcard") String studentId, @Param("year") int year);


    @Query("""
            select e
            from Enrollment e
            where e.student.idcard = :idcard
              and e.course.id = :courseCode
            order by e.year desc
            """)
    List<Enrollment> findEnrollmentYearsOrderedDesc(@Param("idcard") String studentId, @Param("courseCode") int courseCode);
}
