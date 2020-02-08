DROP TABLE vet_specialties IF EXISTS;
DROP TABLE vets IF EXISTS;
DROP TABLE specialties IF EXISTS;
DROP TABLE visits IF EXISTS;
DROP TABLE pets IF EXISTS;
DROP TABLE gender IF EXISTS;
DROP TABLE owners IF EXISTS;


CREATE TABLE vets (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30)
);
CREATE INDEX vets_last_name ON vets (last_name);

CREATE TABLE specialties (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id       INTEGER NOT NULL,
  specialty_id INTEGER NOT NULL
);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_vets FOREIGN KEY (vet_id) REFERENCES vets (id);
ALTER TABLE vet_specialties ADD CONSTRAINT fk_vet_specialties_specialties FOREIGN KEY (specialty_id) REFERENCES specialties (id);

CREATE TABLE gender (
  id   INTEGER IDENTITY PRIMARY KEY,
  name VARCHAR(80)
);
CREATE INDEX gender_name ON gender (name);

CREATE TABLE owners (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR_IGNORECASE(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);
CREATE INDEX owners_last_name ON owners (last_name);

CREATE TABLE pets (
  id         INTEGER IDENTITY PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  gender_id    INTEGER NOT NULL,
  owner_id   INTEGER NOT NULL
);
ALTER TABLE pets ADD CONSTRAINT fk_pets_owners FOREIGN KEY (owner_id) REFERENCES owners  (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_gender FOREIGN KEY (gender_id) REFERENCES gender (id);
CREATE INDEX pets_name ON pets (name);

CREATE TABLE visits (
  id          INTEGER IDENTITY PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  visit_date  TIMESTAMP,
  description VARCHAR(255)
);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets FOREIGN KEY (pet_id) REFERENCES pets (id);
CREATE INDEX visits_pet_id ON visits (pet_id);
