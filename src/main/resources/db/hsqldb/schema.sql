DROP TABLE visits IF EXISTS;
DROP TABLE patients IF EXISTS;
DROP TABLE gender IF EXISTS;
DROP TABLE doctors IF EXISTS;

CREATE TABLE gender (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX gender_name ON gender (name);

CREATE TABLE doctors (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR_IGNORECASE(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX doctors_last_name ON doctors (last_name);

CREATE TABLE patients (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  gender_id    INTEGER NOT NULL,
  doctor_id   INTEGER NOT NULL
);
ALTER TABLE patients ADD CONSTRAINT fk_patients_doctors FOREIGN KEY (doctor_id) REFERENCES doctors  (id);
ALTER TABLE patients ADD CONSTRAINT fk_patients_gender FOREIGN KEY (gender_id) REFERENCES gender (id);
CREATE INDEX patients_name ON patients (name);

CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  patient_id      INTEGER NOT NULL,
  visit_date  TIMESTAMP,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_patients FOREIGN KEY (patient_id) REFERENCES patients (id);
CREATE INDEX visits_patient_id ON visits (patient_id);
