-- Add priorytet column to zgloszenia table
ALTER TABLE zgloszenia ADD COLUMN priorytet VARCHAR(20) NOT NULL DEFAULT 'MEDIUM';

-- Add missing columns that exist in entity but not in original schema  
ALTER TABLE zgloszenia ADD COLUMN tytul VARCHAR(255);
ALTER TABLE zgloszenia ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE zgloszenia ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE zgloszenia ADD COLUMN dzial_id BIGINT REFERENCES dzialy(id);
ALTER TABLE zgloszenia ADD COLUMN autor_id BIGINT REFERENCES users(id);