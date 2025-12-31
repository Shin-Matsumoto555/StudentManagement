DROP TABLE IF EXISTS students;
CREATE TABLE IF NOT EXISTS students (
    student_uuid CHAR(36) DEFAULT (UUID()) PRIMARY KEY, -- ここを MySQL 互換に
    name VARCHAR(50) NOT NULL,
    furigana_name VARCHAR(50),
    nickname VARCHAR(50),
    email VARCHAR(50),
    address VARCHAR(50),
    age INT,
    gender VARCHAR(50),
    remark VARCHAR(255),
    is_deleted boolean
);

CREATE TABLE IF NOT EXISTS student_courses (
    uuid CHAR(36) DEFAULT (UUID()) PRIMARY KEY, -- ここも MySQL 互換に
    student_uuid CHAR(36),
    course_name VARCHAR(50) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_student_courses_students
        FOREIGN KEY (student_uuid) REFERENCES students (student_uuid)
        ON DELETE CASCADE
);