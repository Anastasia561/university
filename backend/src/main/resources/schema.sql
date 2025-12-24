-- Table: users
CREATE TABLE users
(
    id         INTEGER AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(20)  NOT NULL,
    last_name  VARCHAR(20)  NOT NULL,
    email      VARCHAR(50)  NOT NULL,
    password   VARCHAR(250) NOT NULL,
    role       VARCHAR(30)  NOT NULL
);

-- Table: student
CREATE TABLE student
(
    id         INTEGER PRIMARY KEY,
    birth_date DATE        NOT NULL,
    CONSTRAINT student_users_fk FOREIGN KEY (id) REFERENCES users (id)
);

-- Table: course
CREATE TABLE course
(
    id          INTEGER AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    code        VARCHAR(5)   NOT NULL,
    credit      INTEGER      NOT NULL,
    description VARCHAR(100) NOT NULL
);

-- Table: enrollment
CREATE TABLE enrollment
(
    id              INTEGER AUTO_INCREMENT PRIMARY KEY,
    course_id       INTEGER NOT NULL,
    student_id      INTEGER NOT NULL,
    enrollment_date DATE    NOT NULL,
    final_grade     DECIMAL(2, 1),
    CONSTRAINT enrollment_course_fk FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT enrollment_student_fk FOREIGN KEY (student_id) REFERENCES student (id)
);
