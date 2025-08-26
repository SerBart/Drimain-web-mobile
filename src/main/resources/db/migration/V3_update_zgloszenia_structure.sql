-- Update zgloszenia table structure for department-based scoping

-- Add new columns
ALTER TABLE zgloszenia ADD COLUMN tytul VARCHAR(255);
ALTER TABLE zgloszenia ADD COLUMN dzial_id BIGINT;
ALTER TABLE zgloszenia ADD COLUMN autor_id BIGINT;
ALTER TABLE zgloszenia ADD COLUMN created_at TIMESTAMP DEFAULT NOW();
ALTER TABLE zgloszenia ADD COLUMN updated_at TIMESTAMP DEFAULT NOW();

-- Add foreign key constraints
ALTER TABLE zgloszenia ADD CONSTRAINT fk_zgloszenia_dzial 
    FOREIGN KEY (dzial_id) REFERENCES dzialy(id);
ALTER TABLE zgloszenia ADD CONSTRAINT fk_zgloszenia_autor 
    FOREIGN KEY (autor_id) REFERENCES users(id);

-- For existing records, we'll need to handle migration carefully
-- For now, we'll allow nulls temporarily for migration purposes
-- In production, you might want to set default values or clean up existing data

-- Later, we can make these NOT NULL after data migration
-- ALTER TABLE zgloszenia ALTER COLUMN dzial_id SET NOT NULL;
-- ALTER TABLE zgloszenia ALTER COLUMN autor_id SET NOT NULL;