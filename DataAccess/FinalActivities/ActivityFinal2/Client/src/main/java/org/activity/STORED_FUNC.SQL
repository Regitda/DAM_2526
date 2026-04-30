-- passed by a student in a course
CREATE OR REPLACE FUNCTION vtschool.subjects_passed_DG_2526(p_idcard varchar, p_course int)
    RETURNS TABLE
            (
                subject_id int
            )
    LANGUAGE sql
AS
$$
SELECT DISTINCT sc.subject_id
FROM vtschool.enrollments e
         JOIN vtschool.scores sc ON sc.enrollment_id = e.code
WHERE e.student = p_idcard
  AND e.course = p_course
  AND sc.score IS NOT NULL
  AND sc.score >= 5
$$;

-- not passed.
CREATE OR REPLACE FUNCTION vtschool.subjects_not_passed_DG_2526(
    p_idcard varchar,
    p_course int
)
    RETURNS TABLE
            (
                subject_id int
            )
    LANGUAGE sql
AS
$$
SELECT cs.subject_id
FROM vtschool.subject_courses cs
WHERE cs.course_id = p_course
  AND NOT EXISTS (SELECT 1
                  FROM vtschool.subjects_passed_DG_2526(p_idcard, p_course) sp
                  WHERE sp.subject_id = cs.subject_id);
$$;

-- I am unsure if I can create my own?
CREATE OR REPLACE FUNCTION vtschool.year_subject_and_mark_DG_2526(
    p_idcard varchar,
    p_course int
)
    RETURNS TABLE (
                      year        int,
                      subject     varchar,
                      mark        int
                  )
    LANGUAGE sql
AS $$
SELECT
    e.year,
    s.name,
    scr.score
FROM vtschool.enrollments e
         JOIN vtschool.courses c ON c.code = e.course
         JOIN vtschool.subject_courses cs ON cs.course_id = e.course
         JOIN vtschool.subjects s ON s.code = cs.subject_id
         LEFT JOIN vtschool.scores scr
                   ON scr.enrollment_id = e.code
                       AND scr.subject_id    = s.code
WHERE e.student = p_idcard
  AND e.course  = p_course
ORDER BY e.year, s.year, s.name;
$$;