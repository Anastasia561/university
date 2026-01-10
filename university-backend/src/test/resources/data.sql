-- users
INSERT INTO users (first_name, last_name, email, password, role)
VALUES ('Anna', 'Kowalska', 'anna.k@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Jan', 'Nowak', 'jan.n@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Maria', 'Wozniak', 'maria.w@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Piotr', 'Zielinski', 'piotr.z@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Katarzyna', 'Maj', 'katarzyna.m@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Tomasz', 'Lewandowski', 'tomasz.l@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Agnieszka', 'Wojcik', 'agnieszka.w@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Marek', 'Szymanski', 'marek.s@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Ewa', 'Krol', 'ewa.k@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'STUDENT'),
       ('Adam', 'Pawlak', 'adam.p@example.com', '$2a$10$vLrzSWN6uhaiMxMaiKwG4u77Dzu81A4/V.vL.hU0Ns2Gsz56HnIKG', 'ADMIN');
-- pass 111

-- students
INSERT INTO student (id, uuid, birth_date)
VALUES
    (1, '11111111-1111-1111-1111-111111111111', '2001-05-12'),
    (2, '22222222-2222-2222-2222-222222222222', '2000-11-23'),
    (3, '33333333-3333-3333-3333-333333333333', '2002-01-08'),
    (4, '44444444-4444-4444-4444-444444444444', '1999-09-15'),
    (5, '55555555-5555-5555-5555-555555555555', '2001-03-27'),
    (6, '66666666-6666-6666-6666-666666666666', '2000-07-04'),
    (7, '77777777-7777-7777-7777-777777777777', '2000-07-04'),
    (8, '88888888-8888-8888-8888-888888888888', '2002-06-19'),
    (9, '99999999-9999-9999-9999-999999999999', '2001-10-10');



-- courses
INSERT INTO course (uuid, name, code, credit, description)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Algorithms', 'ALG', 5, 'Study of algorithms'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Databases', 'DB', 4, 'Introduction to relational databases'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Operating Systems', 'OS', 5, 'Principles of operating systems'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'Web Development', 'WEB', 4, 'Frontend and backend basics'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Computer Networks', 'NET', 4, 'Networking fundamentals'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'Software Engineering', 'SE', 3, 'Software development principles'),
    ('11111111-aaaa-bbbb-cccc-111111111111', 'Data Structures', 'DS', 5, 'Structures for data storage'),
    ('22222222-aaaa-bbbb-cccc-222222222222', 'Machine Learning', 'ML', 5, 'Intro to ML concepts'),
    ('33333333-aaaa-bbbb-cccc-333333333333', 'Cloud Computing', 'CLD', 4, 'Cloud platforms and services'),
    ('44444444-aaaa-bbbb-cccc-444444444444', 'Cybersecurity', 'SEC', 3, 'Basics of security and threats');

--enrollments
INSERT INTO enrollment (uuid, course_id, enrollment_date, final_grade, student_id)
VALUES
    ('aaaa1111-1111-1111-1111-111111111111', 1, '2024-02-10', 4.0, 1),
    ('bbbb2222-2222-2222-2222-222222222222', 3, '2030-02-11', 5.0, 1),
    ('cccc3333-3333-3333-3333-333333333333', 2, '2024-02-12', 3.5, 2),
    ('dddd4444-4444-4444-4444-444444444444', 3, '2024-03-01', 5.0, 3),
    ('eeee5555-5555-5555-5555-555555555555', 4, '2024-03-15', 4.5, 4);
