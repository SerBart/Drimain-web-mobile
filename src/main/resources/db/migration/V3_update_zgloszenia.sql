-- Update zgloszenia table to add new required fields
ALTER TABLE zgloszenia 
    ADD COLUMN tytul VARCHAR(255),
    ADD COLUMN dzial_id BIGINT REFERENCES dzialy(id),
    ADD COLUMN autor_id BIGINT REFERENCES users(id),
    ADD COLUMN created_at TIMESTAMP DEFAULT NOW(),
    ADD COLUMN updated_at TIMESTAMP;

-- Update existing records to have reasonable defaults (will need manual cleanup)
UPDATE zgloszenia SET 
    tytul = COALESCE(typ, 'Legacy Report'),
    created_at = COALESCE(dataGodzina, NOW())
WHERE tytul IS NULL;

-- Make required columns NOT NULL after setting defaults
ALTER TABLE zgloszenia 
    ALTER COLUMN tytul SET NOT NULL,
    ALTER COLUMN created_at SET NOT NULL;