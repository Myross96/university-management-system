DROP TYPE IF EXISTS gender CASCADE;
CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');
CREATE CAST (character varying as gender) WITH INOUT AS IMPLICIT;

DROP TYPE IF EXISTS academic_degree CASCADE;
CREATE TYPE academic_degree AS ENUM ('BACHELOR', 'MASTER', 'DOCTOR');
CREATE CAST (character varying as academic_degree) WITH INOUT AS IMPLICIT;

DROP TABLE IF EXISTS courses CASCADE;
CREATE TABLE courses(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    description TEXT
);

DROP TABLE IF EXISTS lectures_times CASCADE;
CREATE TABLE lectures_times(
    id SERIAL PRIMARY KEY,
    start_time time,
    end_time time
);

DROP TABLE IF EXISTS audiences CASCADE;
CREATE TABLE audiences(
    id SERIAL PRIMARY KEY,
    number INT NOT NULL,
    capacity INT NOT NULL   
);

DROP TABLE IF EXISTS holidays CASCADE;
CREATE TABLE holidays(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    date DATE   
);

DROP TABLE IF EXISTS groups CASCADE;
CREATE TABLE groups(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

DROP TABLE IF EXISTS teachers CASCADE;
CREATE TABLE teachers(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    birth_date DATE,
    gender gender,
    email VARCHAR(50),
    address VARCHAR(50),
    post_code VARCHAR(50),
    degree academic_degree,
    experience INT
);

DROP TABLE IF EXISTS vacations CASCADE;
CREATE TABLE vacations(
    id SERIAL PRIMARY KEY,
    start_date DATE,
    end_date DATE,
    teacher_id INT REFERENCES teachers(id)
);

DROP TABLE IF EXISTS students CASCADE;
CREATE TABLE students(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    gender gender,
    birth_date DATE,
    address VARCHAR(255),
    email VARCHAR(50),
    post_code VARCHAR(50),
    group_id INT REFERENCES groups(id)
);

DROP TABLE IF EXISTS lectures CASCADE;
CREATE TABLE lectures(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    audience_id INT REFERENCES audiences(id),
    course_id INT REFERENCES courses(id),
    teacher_id INT REFERENCES teachers(id),
    lecture_time_id INT REFERENCES lectures_times(id),
    date DATE
);

DROP TABLE IF EXISTS groups_courses CASCADE;
CREATE TABLE groups_courses(
    group_id INT REFERENCES groups(id) ON DELETE CASCADE,
    course_id INT REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE(group_id, course_id)
);

DROP TABLE IF EXISTS lectures_groups CASCADE;
CREATE TABLE lectures_groups(
    lecture_id INT REFERENCES lectures(id) ON DELETE CASCADE,
    group_id INT REFERENCES groups(id) ON DELETE CASCADE,
    UNIQUE(lecture_id, group_id)
);

DROP TABLE IF EXISTS teachers_courses CASCADE;
CREATE TABLE teachers_courses(
    teacher_id INT REFERENCES teachers(id) ON DELETE CASCADE,
    course_id INT REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE(teacher_id, course_id)
);

DROP TABLE IF EXISTS teachers_groups CASCADE;
CREATE TABLE teachers_groups(
    teacher_id INT REFERENCES teachers(id) ON DELETE CASCADE,
    group_id INT REFERENCES groups(id) ON DELETE CASCADE,
    UNIQUE(teacher_id, group_id)
);