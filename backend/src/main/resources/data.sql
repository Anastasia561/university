-- users
INSERT INTO users (first_name, last_name, email, password, role)
VALUES ('Anna', 'Kowalska', 'anna.k@example.com', 'pass1', 'STUDENT'),
       ('Jan', 'Nowak', 'jan.n@example.com', 'pass2', 'STUDENT'),
       ('Maria', 'Wozniak', 'maria.w@example.com', 'pass3', 'STUDENT'),
       ('Piotr', 'Zielinski', 'piotr.z@example.com', 'pass4', 'STUDENT'),
       ('Katarzyna', 'Maj', 'katarzyna.m@example.com', 'pass5', 'STUDENT'),
       ('Tomasz', 'Lewandowski', 'tomasz.l@example.com', 'pass6', 'STUDENT'),
       ('Agnieszka', 'Wojcik', 'agnieszka.w@example.com', 'pass7', 'STUDENT'),
       ('Marek', 'Szymanski', 'marek.s@example.com', 'pass8', 'STUDENT'),
       ('Ewa', 'Krol', 'ewa.k@example.com', 'pass9', 'STUDENT'),
       ('Adam', 'Pawlak', 'adam.p@example.com', 'pass10', 'ADMIN');

-- students
INSERT INTO student (id, birth_date)
VALUES (1, '2001-05-12'),
       (2, '2000-11-23'),
       (3, '2002-01-08'),
       (4, '1999-09-15'),
       (5, '2001-03-27'),
       (6, '2000-07-04'),
       (7, '1998-12-30'),
       (8, '2002-06-19'),
       (9, '2001-10-10');

-- courses
INSERT INTO course (name, code, credit, description)
VALUES ('Algorithms', 'ALG', 5, 'Study of algorithms'),
       ('Databases', 'DB', 4, 'Introduction to relational databases'),
       ('Operating Systems', 'OS', 5, 'Principles of operating systems'),
       ('Web Development', 'WEB', 4, 'Frontend and backend basics'),
       ('Computer Networks', 'NET', 4, 'Networking fundamentals'),
       ('Software Engineering', 'SE', 3, 'Software development principles'),
       ('Data Structures', 'DS', 5, 'Structures for data storage'),
       ('Machine Learning', 'ML', 5, 'Intro to ML concepts'),
       ('Cloud Computing', 'CLD', 4, 'Cloud platforms and services'),
       ('Cybersecurity', 'SEC', 3, 'Basics of security and threats');

--enrollments
INSERT INTO enrollment (course_id, enrollment_date, final_grade, student_id)
VALUES (1, '2024-02-10', 4.0, 1),
       (3, '2024-02-11', 5.0, 1),
       (2, '2024-02-12', 3.5, 2),
       (3, '2024-03-01', 5.0, 3),
       (4, '2024-03-15', 4.5, 4),
       (5, '2024-04-01', 3.0, 5),
       (6, '2024-04-10', 4.0, 6),
       (7, '2024-05-02', 5.0, 7),
       (8, '2024-05-15', 3.5, 8),
       (9, '2024-06-01', 4.0, 9);
