-- Migration to add priority field and other missing fields to zgloszenia table
-- This supports the PR1 requirements for priority, validation, and auditing

ALTER TABLE zgloszenia 
ADD COLUMN priorytet VARCHAR(20) DEFAULT 'NORMALNY';

-- Add other missing columns that are in the entity but not in the original schema
ALTER TABLE zgloszenia 
ADD COLUMN tytul VARCHAR(200),
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN dzial_id BIGINT REFERENCES dzialy(id),
ADD COLUMN autor_id BIGINT REFERENCES users(id);

-- Update existing records to have proper timestamps if they don't exist
UPDATE zgloszenia 
SET created_at = COALESCE(dataGodzina, CURRENT_TIMESTAMP),
    updated_at = COALESCE(dataGodzina, CURRENT_TIMESTAMP)
WHERE created_at IS NULL OR updated_at IS NULL;

-- Make timestamp columns NOT NULL after setting values
ALTER TABLE zgloszenia 
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;

-- Add check constraint for priority values
ALTER TABLE zgloszenia 
ADD CONSTRAINT chk_zgloszenia_priorytet 
CHECK (priorytet IN ('NISKI', 'NORMALNY', 'WYSOKI', 'KRYTYCZNY'));

-- Create index on priority for better query performance  
CREATE INDEX idx_zgloszenia_priorytet ON zgloszenia(priorytet);

-- Create index on status for existing queries
CREATE INDEX idx_zgloszenia_status ON zgloszenia(status);

-- Create index on created_at for temporal queries
CREATE INDEX idx_zgloszenia_created_at ON zgloszenia(created_at);