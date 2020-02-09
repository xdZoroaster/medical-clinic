
CREATE TABLE IF NOT EXISTS gender
(
    id   INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80),
    INDEX (name)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS doctors
(
    id         INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30),
    last_name  VARCHAR(30),
    address    VARCHAR(255),
    city       VARCHAR(80),
    telephone  VARCHAR(20),
    INDEX (last_name)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS patients
(
    id         INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(30),
    birth_date DATE,
    gender_id  INT(4) UNSIGNED NOT NULL,
    doctor_id  INT(4) UNSIGNED NOT NULL,
    INDEX (name),
    FOREIGN KEY (doctor_id) REFERENCES doctors (id),
    FOREIGN KEY (gender_id) REFERENCES gender (id)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS visits
(
    id          INT(4) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    patient_id  INT(4) UNSIGNED NOT NULL,
    visit_date  DATE,
    description VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES patients (id)
) engine = InnoDB;
