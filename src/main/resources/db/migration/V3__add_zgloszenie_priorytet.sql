-- V3: Dodanie kolumny priorytet do tabeli 'zgloszenia'
-- Kontekst:
--   V1_initial_schema.sql - utworzyła bazowe tabele (zgloszenia bez kolumn audytowych i priorytetu)
--   V2__create_attachment_table.sql - dodała: created_at, updated_at, dzial_id, autor_id, tytul oraz tabelę attachments
-- Problem:
--   Encja Zgloszenie posiada pole 'priorytet' (Enum STRING) z domyślną wartością NORMALNY, którego brakowało w DB.
-- Działanie:
--   Dodajemy kolumnę priorytet z wartościami tekstowymi ENUM (NISKI, NORMALNY, WYSOKI, KRYTYCZNY) – nie tworzymy typu ENUM w Postgres.
-- Uwaga:
--   IF NOT EXISTS pozwala przejść migracji, jeśli ktoś lokalnie już dodał kolumnę ręcznie.

ALTER TABLE zgloszenia
    ADD COLUMN IF NOT EXISTS priorytet VARCHAR(32);

-- Ustaw domyślną wartość dla istniejących rekordów
UPDATE zgloszenia
SET priorytet = 'NORMALNY'
WHERE priorytet IS NULL;

-- Wymuś NOT NULL po uzupełnieniu
ALTER TABLE zgloszenia
    ALTER COLUMN priorytet SET NOT NULL;

-- (Opcjonalnie) CHECK constraint (odkomentuj jeśli chcesz dodatkowe ograniczenie)
-- ALTER TABLE zgloszenia
--     ADD CONSTRAINT chk_zgloszenia_priorytet
--     CHECK (priorytet IN ('NISKI','NORMALNY','WYSOKI','KRYTYCZNY'));

-- Koniec migracji V3__add_zgloszenie_priorytet.sql