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

