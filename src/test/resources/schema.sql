DROP TABLE IF EXISTS students;
CREATE TABLE IF NOT EXISTS students (
    student_uuid CHAR(36) DEFAULT (UUID()) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    furigana_name VARCHAR(50),
    nickname VARCHAR(50),
    email VARCHAR(50),
    address VARCHAR(50),
    age INT,
    gender VARCHAR(50),
    remark VARCHAR(255),
    is_deleted boolean NOT NULL DEFAULT false -- NOT NULL と DEFAULT を追加
);

-- student_courses の主キー名を uuid から course_uuid に変更
-- student_courses に is_deleted を追加
CREATE TABLE IF NOT EXISTS student_courses (
    course_uuid CHAR(36) DEFAULT (UUID()) PRIMARY KEY,
    student_uuid CHAR(36),
    course_name VARCHAR(50) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    is_deleted boolean NOT NULL DEFAULT false, -- NOT NULL と DEFAULT を追加
    CONSTRAINT fk_student_courses_students
        FOREIGN KEY (student_uuid) REFERENCES students (student_uuid)
        ON DELETE CASCADE
);

-- これに合わせて application_status の外部キー参照先も変更
-- application_status に is_deleted を追加
CREATE TABLE IF NOT EXISTS application_status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status_uuid CHAR(36) DEFAULT (UUID()),
    course_uuid CHAR(36) NOT NULL,
    status VARCHAR(50) NOT NULL,
    is_deleted boolean NOT NULL DEFAULT false, -- NOT NULL と DEFAULT を追加
    CONSTRAINT fk_application_status_courses
        FOREIGN KEY (course_uuid) REFERENCES student_courses (course_uuid)
        ON DELETE CASCADE
);