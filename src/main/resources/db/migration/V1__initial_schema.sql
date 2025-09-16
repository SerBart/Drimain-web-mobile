CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE dzialy (
    id BIGSERIAL PRIMARY KEY,
    nazwa VARCHAR(255) NOT NULL
);

CREATE TABLE maszyny (
    id BIGSERIAL PRIMARY KEY,
    nazwa VARCHAR(255) NOT NULL,
    dzial_id BIGINT REFERENCES dzialy(id)
);

CREATE TABLE osoby (
    id BIGSERIAL PRIMARY KEY,
    login VARCHAR(255),
    haslo VARCHAR(255),
    imie_nazwisko VARCHAR(255),
    rola VARCHAR(255)
);

CREATE TABLE parts (
    id BIGSERIAL PRIMARY KEY,
    nazwa VARCHAR(255),
    kod VARCHAR(255),
    kategoria VARCHAR(255),
    ilosc INTEGER,
    minIlosc INTEGER,
    jednostka VARCHAR(32)
);

CREATE TABLE raporty (
    id BIGSERIAL PRIMARY KEY,
    maszyna_id BIGINT REFERENCES maszyny(id),
    typ_naprawy VARCHAR(255),
    opis VARCHAR(4000),
    osoba_id BIGINT REFERENCES osoby(id),
    status VARCHAR(40),
    data_naprawy DATE,
    czas_od TIME,
    czas_do TIME
);

CREATE TABLE part_usages (
    id BIGSERIAL PRIMARY KEY,
    raport_id BIGINT REFERENCES raporty(id) ON DELETE CASCADE,
    part_id BIGINT REFERENCES parts(id),
    ilosc INTEGER
);

-- Completed zgloszenia to reflect current code usage
CREATE TABLE zgloszenia (
    id BIGSERIAL PRIMARY KEY,
    data_godzina TIMESTAMP,
    typ VARCHAR(255),
    imie VARCHAR(255),
    nazwisko VARCHAR(255),
    tytul VARCHAR(255),
    status VARCHAR(40),
    priorytet VARCHAR(40),
    opis VARCHAR(4000),
    photo BYTEA,
    dzial_id BIGINT REFERENCES dzialy(id),
    autor_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- New table used by Attachment feature
CREATE TABLE attachments (
    id BIGSERIAL PRIMARY KEY,
    zgloszenie_id BIGINT NOT NULL REFERENCES zgloszenia(id) ON DELETE CASCADE,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL UNIQUE,
    content_type VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    checksum VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_attachments_zgloszenie ON attachments(zgloszenie_id);

-- Inventory module (Magazyn)
CREATE TABLE czesci_magazyn (
    id BIGSERIAL PRIMARY KEY,
    nazwa VARCHAR(255),
    numer_katalogowy VARCHAR(255),
    dostawca VARCHAR(255),
    producent VARCHAR(255),
    ilosc INTEGER,
    data_dodania DATE
);