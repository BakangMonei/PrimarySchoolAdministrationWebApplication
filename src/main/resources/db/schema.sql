CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    role VARCHAR(16) NOT NULL,
    email VARCHAR(128),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    remember_me_token VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS parents (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    name VARCHAR(64) NOT NULL,
    surname VARCHAR(64) NOT NULL,
    contact_no VARCHAR(32),
    email VARCHAR(128),
    children_ids TEXT,
    CONSTRAINT fk_parent_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS teachers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    surname VARCHAR(64) NOT NULL,
    omang_or_passport_no VARCHAR(16) NOT NULL UNIQUE,
    gender VARCHAR(10),
    date_of_birth DATE,
    address VARCHAR(255),
    contact_no VARCHAR(32),
    email VARCHAR(128),
    qualifications TEXT,
    subjects_qualified TEXT,
    class_subject_map TEXT,
    date_joined DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS classes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(255),
    class_teacher_id BIGINT,
    capacity INT DEFAULT 35,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_class_teacher FOREIGN KEY (class_teacher_id) REFERENCES teachers(id)
);

CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS students (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL,
    surname VARCHAR(64) NOT NULL,
    birth_certificate_no VARCHAR(9) NOT NULL UNIQUE,
    gender VARCHAR(10),
    date_of_birth DATE,
    address VARCHAR(255),
    guardian_name VARCHAR(128),
    guardian_contact_no VARCHAR(32),
    guardian_email VARCHAR(128),
    registration_date DATE,
    status VARCHAR(16),
    current_class VARCHAR(64),
    subjects TEXT,
    subject_grades TEXT,
    average_grade DECIMAL(5,2) DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS grade_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    grade DECIMAL(5,2) NOT NULL,
    teacher_id BIGINT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    term VARCHAR(16),
    comment VARCHAR(255),
    CONSTRAINT fk_grade_student FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_grade_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_grade_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);

-- Seed data ---------------------------------------------------------------

INSERT INTO users (username, hashed_password, role, email, active)
VALUES ('admin', '$2a$12$4rS9AqvOB0fOkH1ZZ1xd6eyWzxubUANe0yoyS9MhUlCT3VkOITkk6', 'ADMIN', 'admin@school.org', TRUE)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO users (username, hashed_password, role, email, active)
VALUES ('teacher.mathi', '$2a$12$4rS9AqvOB0fOkH1ZZ1xd6eyWzxubUANe0yoyS9MhUlCT3VkOITkk6', 'TEACHER', 'mathi@school.org', TRUE)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO users (username, hashed_password, role, email, active)
VALUES ('parent.moyo', '$2a$12$4rS9AqvOB0fOkH1ZZ1xd6eyWzxubUANe0yoyS9MhUlCT3VkOITkk6', 'PARENT', 'parent.moyo@example.com', TRUE)
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO parents (user_id, name, surname, contact_no, email, children_ids)
SELECT id, 'Mpho', 'Moyo', '+267 74000001', 'parent.moyo@example.com', NULL
FROM users WHERE username = 'parent.moyo'
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO teachers (name, surname, omang_or_passport_no, gender, date_of_birth, address, contact_no, email,
                      qualifications, subjects_qualified, class_subject_map, date_joined)
VALUES ('Itumeleng', 'Mathibe', '1234567890123', 'Female', '1989-05-14', 'Plot 120, Tribal Road, Mochudi',
        '+267 73000002', 'mathi@school.org', 'B.Ed Primary Education',
        'Mathematics,Science', 'Standard 4:Mathematics|Standard 5:Science', '2015-01-07')
ON DUPLICATE KEY UPDATE email = VALUES(email);

INSERT INTO subjects (name, description) VALUES
    ('Mathematics', 'Numeracy, problem solving, and arithmetic fundamentals.'),
    ('Science', 'Natural sciences and investigation skills.'),
    ('English', 'Language arts, comprehension, and writing.')
ON DUPLICATE KEY UPDATE description = VALUES(description);

INSERT INTO classes (name, description, class_teacher_id, capacity)
VALUES ('Standard 4', 'Intermediate learners aged 10-11', (SELECT id FROM teachers ORDER BY id LIMIT 1), 30)
ON DUPLICATE KEY UPDATE description = VALUES(description);

INSERT INTO students (name, surname, birth_certificate_no, gender, date_of_birth, address,
                      guardian_name, guardian_contact_no, guardian_email, registration_date,
                      status, current_class, subjects, subject_grades, average_grade, notes)
VALUES ('Aobakwe', 'Sesinyi', '123456789', 'Male', '2014-09-12', 'Plot 55, Village Center, Mochudi',
        'Mpho Moyo', '+267 74000001', 'parent.moyo@example.com', '2022-01-10',
        'Active', 'Standard 4', 'Mathematics,Science,English', 'Mathematics:78|Science:82|English:74', 78.0,
        'Promising in sciences')
ON DUPLICATE KEY UPDATE status = VALUES(status);

INSERT INTO grade_records (student_id, subject_id, grade, teacher_id, term, comment)
SELECT s.id, sub.id, 82.0, t.id, 'Term 1', 'Excellent practical work'
FROM students s
JOIN subjects sub ON sub.name = 'Science'
JOIN teachers t ON t.omang_or_passport_no = '1234567890123'
WHERE s.birth_certificate_no = '123456789'
ON DUPLICATE KEY UPDATE grade = VALUES(grade);

