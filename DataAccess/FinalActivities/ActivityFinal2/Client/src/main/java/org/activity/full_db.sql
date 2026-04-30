DROP SCHEMA IF EXISTS vtschool CASCADE;
CREATE SCHEMA vtschool;


SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET search_path = vtschool;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 201 (class 1259 OID 16424)
-- Name: subjects; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE subjects (
                          code integer NOT NULL,
                          name character varying(50),
                          year integer,
                          hours integer
);

ALTER TABLE subjects OWNER TO postgres;

CREATE SEQUENCE subjects_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;


ALTER TABLE subjects_code_seq OWNER TO postgres;

ALTER SEQUENCE subjects_code_seq OWNED BY subjects.code;

CREATE TABLE courses (
                         code integer NOT NULL,
                         name character varying(90) NOT NULL
);

ALTER TABLE courses OWNER TO postgres;

CREATE SEQUENCE courses_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

ALTER TABLE courses_code_seq OWNER TO postgres;

ALTER SEQUENCE courses_code_seq OWNED BY courses.code;

--
-- TOC entry 207 (class 1259 OID 16469)
-- Name: enrollments; Type: TABLE; Schema: public; Owner: postgres
--

-- Creamos una secuencia para la tabla intermedia
CREATE SEQUENCE subject_courses_code_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 2147483647
    CACHE 1;

-- Creamos la tabla intermedia
CREATE TABLE subject_courses (
                                 code integer NOT NULL DEFAULT nextval('subject_courses_code_seq'::regclass),
                                 subject_id integer NOT NULL,
                                 course_id integer NOT NULL
);

CREATE TABLE enrollments (
                             student character varying(12) NOT NULL,
                             course integer NOT NULL,
                             year integer NOT NULL,
                             code integer NOT NULL
);

ALTER TABLE enrollments OWNER TO postgres;

--
-- TOC entry 206 (class 1259 OID 16467)
-- Name: inscriptions_code_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE inscriptions_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE inscriptions_code_seq OWNER TO postgres;

--
-- TOC entry 3045 (class 0 OID 0)
-- Dependencies: 206
-- Name: inscriptions_code_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE inscriptions_code_seq OWNED BY enrollments.code;

--
-- TOC entry 208 (class 1259 OID 16485)
-- Name: scores; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE scores (
                        enrollment_id integer NOT NULL,
                        subject_id integer NOT NULL,
                        score integer,
                        code integer NOT NULL
);

ALTER TABLE scores OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16462)
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE SEQUENCE scores_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE scores_code_seq OWNER TO postgres;

--
-- TOC entry 3045 (class 0 OID 0)
-- Dependencies: 206
-- Name: inscriptions_code_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE scores_code_seq OWNED BY scores.code;

CREATE TABLE students (
                          firstname character varying(50) NOT NULL,
                          lastname character varying(100) NOT NULL,
                          idcard character varying(8) NOT NULL,
                          phone character varying(12),
                          email character varying(100)
);


ALTER TABLE students OWNER TO postgres;

ALTER TABLE ONLY courses ALTER COLUMN code SET DEFAULT nextval('courses_code_seq'::regclass);

ALTER TABLE ONLY enrollments ALTER COLUMN code SET DEFAULT nextval('inscriptions_code_seq'::regclass);

ALTER TABLE ONLY subjects ALTER COLUMN code SET DEFAULT nextval('subjects_code_seq'::regclass);

ALTER TABLE ONLY scores ALTER COLUMN code SET DEFAULT nextval('scores_code_seq'::regclass);


--
-- TOC entry 3033 (class 0 OID 16437)
-- Dependencies: 203
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO courses (name) VALUES ('Multiplatform app development');
INSERT INTO courses (name) VALUES ('Web development');


--
-- TOC entry 3036 (class 0 OID 16469)
-- Dependencies: 207
-- Data for Name: enrollments; Type: TABLE DATA; Schema: public; Owner: postgres
--

-- 2023: primer año
INSERT INTO enrollments (student, course, year) VALUES ('12332001', 1, 2023); -- Jose Garcia (DAM 1º)
INSERT INTO enrollments (student, course, year) VALUES ('12332003', 1, 2023); -- Aitana Garcia (DAM 1º)
INSERT INTO enrollments (student, course, year) VALUES ('12332005', 2, 2023); -- John Smith (DAW 1º)
INSERT INTO enrollments (student, course, year) VALUES ('12332004', 2, 2023); -- John Spencer (DAW 1º)

-- 2024: segundo año (los que siguieron avanzan)
INSERT INTO enrollments (student, course, year) VALUES ('12332001', 1, 2024); -- Jose Garcia (DAM 2º)
INSERT INTO enrollments (student, course, year) VALUES ('12332003', 2, 2024); -- Aitana Garcia (DAW 2º)
INSERT INTO enrollments (student, course, year) VALUES ('12332005', 2, 2024); -- John Smith (DAW 2º)
INSERT INTO enrollments (student, course, year) VALUES ('12332004', 1, 2024); -- John Spencer (DAM 2º)
INSERT INTO enrollments (student, course, year) VALUES ('12332006', 1, 2024); -- Marcos Andreu (DAM 1º)
INSERT INTO enrollments (student, course, year) VALUES ('12332111', 2, 2024); -- Robe Iniesta (DAW 1º)


--
-- TOC entry 3037 (class 0 OID 16485)
-- Dependencies: 208
-- Data for Name: scores; Type: TABLE DATA; Schema: public; Owner: postgres
--

-- 2023 - DAM
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 6, 7); -- Markup languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 2, 8); -- Database Management Systems
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 5, 4); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 7, 4); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (1, 4, 7); -- Technical English


INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 6, 9); -- Markup languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 2, 10); -- Database Management Systems
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 5, 3); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 7, 4); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (2, 4, 7); -- Technical English

-- 2023 - DAW
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 2, 7); -- Database Management Systems
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 5, 8); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 6, 6); -- Markup Languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 7, 7); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (3, 4, 7); -- Technical English

INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 4, 3); -- Technical English
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 6, 3); -- Markup Languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 7, 8); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 2, 7); -- Database Management Systems
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (4, 5, 8); -- Development Environments

-- 2024 - DAM
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 3, 8); -- Services and Processes
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 5, 9); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 7, 7); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (5, 1, 6); -- Data Access

INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 8, 10); -- Client-side development
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 9, 9);  -- Server-side development
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 5, 10); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (6, 7, 8); -- Programming

-- 2024 - DAW
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (7, 8, 8); -- Client-side development
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (7, 9, 6); -- Server-side development

INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 4, 6); -- Technical English
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 6, 8); -- Markup Languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 1, 7); -- Data Access
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (8, 3, 4); -- Services and Processes

INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 6, 7); -- Markup languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 2, 8); -- Database Management Systems
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 5, 3); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 7, 4); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (9, 4, 7); -- Technical English


INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 6, 9); -- Markup languages
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 2, 8); -- Database Management Systems
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 5, 6); -- Development Environments
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 7, 6); -- Programming
INSERT INTO scores (enrollment_id, subject_id, score) VALUES (10, 4, 7); -- Technical English
--
-- TOC entry 3034 (class 0 OID 16462)
-- Dependencies: 205
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Aitana', 'Garcia', '12332003', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('John', 'Spencer', '12332004', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('John', 'Smith', '12332005', '654654654', 'johnsmith@email.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Marcos', 'Andreu', '12332006', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Student', 'X', '12332007', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Mark', 'Ross', '12332008', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Estrella', 'Garcia', '12332002', '', 'estrella@email.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Robe', 'Iniesta', '12332111', '', '');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Jose', 'Garcia', '12332001', '655565566', 'jrgarcia@mail.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Ken', 'Brockman', '12332321', '123456789', 'ken@e.com');
INSERT INTO students (firstname, lastname, idcard, phone, email) VALUES ('Kevin', 'Smith', '12332444', '123456789', '');


--
-- TOC entry 3031 (class 0 OID 16424)
-- Dependencies: 201
-- Data for Name: subjects; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO subjects (name, year, hours) VALUES ('Data Access', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Database Management Systems', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Services and Processes', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Technical English', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Development Environments', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Markup Languages', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Programming', 1, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Client-side development', 2, NULL);
INSERT INTO subjects (name, year, hours) VALUES ('Server-side development', 2, NULL);

-- DAM (course_id = 1)

INSERT INTO subject_courses (subject_id, course_id) VALUES (2, 1); -- Database Management Systems (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (4, 1); -- Technical English
INSERT INTO subject_courses (subject_id, course_id) VALUES (5, 1); -- Development Environments (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (6, 1); -- Markup Languages (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (7, 1); -- Programming (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (1, 1); -- Data Access
INSERT INTO subject_courses (subject_id, course_id) VALUES (3, 1); -- Services and Processes

-- DAW (course_id = 2)
INSERT INTO subject_courses (subject_id, course_id) VALUES (2, 2); -- Database Management Systems (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (5, 2); -- Development Environments (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (6, 2); -- Markup Languages (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (7, 2); -- Programming (shared)
INSERT INTO subject_courses (subject_id, course_id) VALUES (4, 2); -- Technical English
INSERT INTO subject_courses (subject_id, course_id) VALUES (8, 2); -- Client-side development
INSERT INTO subject_courses (subject_id, course_id) VALUES (9, 2); -- Server-side development

--
-- TOC entry 3047 (class 0 OID 0)
-- Dependencies: 202
-- Name: courses_code_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('courses_code_seq', 2, true);


--
-- TOC entry 3048 (class 0 OID 0)
-- Dependencies: 206
-- Name: inscriptions_code_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('inscriptions_code_seq', 12, true);


--
-- TOC entry 2885 (class 2606 OID 16429)
-- Name: subjects Courses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY subjects
    ADD CONSTRAINT pk_subjects PRIMARY KEY (code);


--
-- TOC entry 2888 (class 2606 OID 16442)
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY courses
    ADD CONSTRAINT pk_courses PRIMARY KEY (code);


--
-- TOC entry 2892 (class 2606 OID 16474)
-- Name: enrollments inscriptions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY enrollments
    ADD CONSTRAINT pk_inscriptions PRIMARY KEY (code);


--
-- TOC entry 2894 (class 2606 OID 16489)
-- Name: scores pk_enrollment; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT pk_scores PRIMARY KEY (code);


ALTER TABLE ONLY scores
    ADD CONSTRAINT ur_scores UNIQUE (enrollment_id, subject_id);


--
-- TOC entry 2890 (class 2606 OID 16466)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY students
    ADD CONSTRAINT pk_students PRIMARY KEY (idcard);

--
-- TOC entry 2897 (class 2606 OID 16480)
-- Name: enrollments fk_course; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY enrollments
    ADD CONSTRAINT fk_course FOREIGN KEY (course) REFERENCES courses(code) NOT VALID;


--
-- TOC entry 2899 (class 2606 OID 16490)
-- Name: scores fk_enrollment; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT fk_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(code);


--
-- TOC entry 2896 (class 2606 OID 16475)
-- Name: enrollments fk_student; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY enrollments
    ADD CONSTRAINT fk_student FOREIGN KEY (student) REFERENCES students(idcard) NOT VALID;


--
-- TOC entry 2898 (class 2606 OID 16495)
-- Name: scores fk_subjects; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY scores
    ADD CONSTRAINT fk_subjects FOREIGN KEY (subject_id) REFERENCES subjects(code);


ALTER TABLE ONLY subject_courses
    ADD CONSTRAINT ur_subject_courses UNIQUE (subject_id, course_id);

ALTER TABLE ONLY subject_courses
    ADD CONSTRAINT pk_subject_courses PRIMARY KEY (code);

ALTER TABLE ONLY subject_courses
    ADD CONSTRAINT fk_subject_courses_course FOREIGN KEY (course_id) REFERENCES courses(code) NOT VALID;

ALTER TABLE ONLY subject_courses
    ADD CONSTRAINT fk_subject_courses_subject FOREIGN KEY (subject_id) REFERENCES subjects(code) NOT VALID;

-- Completed on 2021-12-01 18:16:48

--
-- PostgreSQL database dump complete
--

-- passed by a student in a course
CREATE OR REPLACE FUNCTION vtschool.subjects_passed_DG_2526(p_idcard varchar, p_course int)
RETURNS TABLE(subject_id int)
LANGUAGE sql
AS $$
SELECT DISTINCT sc.subject_id
FROM vtschool.enrollments e
         JOIN vtschool.scores sc ON sc.enrollment_id = e.code
WHERE e.student = p_idcard
  AND e.course = p_course
  AND sc.score IS NOT NULL
  AND sc.score >= 5
    $$;


CREATE OR REPLACE FUNCTION vtschool.subjects_not_passed_DG_2526(
    p_idcard varchar,
    p_course int,
    c_year int
)
    RETURNS TABLE(subject_id int)
    LANGUAGE sql
AS $$
SELECT
    cs.subject_id

FROM vtschool.subject_courses cs
         JOIN vtschool.enrollments e
              ON e.student = p_idcard
                  AND e.course  = p_course
                  AND e.year    = c_year
         LEFT JOIN vtschool.scores scr
                   ON scr.enrollment_id = e.code
                       AND scr.subject_id    = cs.subject_id
WHERE cs.course_id = p_course
  AND (scr.score IS NULL OR scr.score < 5);
$$;

