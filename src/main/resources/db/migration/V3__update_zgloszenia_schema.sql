-- Update zgloszenia table structure to support both old and new schema
-- Add new columns while preserving existing ones

-- Add new columns for the new schema if they don't exist
DO $$
BEGIN
    -- Add tytul column
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'zgloszenia' 
        AND column_name = 'tytul'
    ) THEN
        ALTER TABLE zgloszenia ADD COLUMN tytul VARCHAR(200);
    END IF;
    
    -- Add dzial_id column and FK constraint
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'zgloszenia' 
        AND column_name = 'dzial_id'
    ) THEN
        ALTER TABLE zgloszenia ADD COLUMN dzial_id BIGINT;
        ALTER TABLE zgloszenia ADD CONSTRAINT fk_zgloszenia_dzial 
            FOREIGN KEY (dzial_id) REFERENCES dzialy(id);
        CREATE INDEX idx_zgloszenia_dzial_id ON zgloszenia(dzial_id);
    END IF;
    
    -- Add autor_id column and FK constraint
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'zgloszenia' 
        AND column_name = 'autor_id'
    ) THEN
        ALTER TABLE zgloszenia ADD COLUMN autor_id BIGINT;
        ALTER TABLE zgloszenia ADD CONSTRAINT fk_zgloszenia_autor 
            FOREIGN KEY (autor_id) REFERENCES users(id);
        CREATE INDEX idx_zgloszenia_autor_id ON zgloszenia(autor_id);
    END IF;
    
    -- Create status index if it doesn't exist
    IF NOT EXISTS (
        SELECT 1 FROM pg_indexes 
        WHERE tablename = 'zgloszenia' 
        AND indexname = 'idx_zgloszenia_status'
    ) THEN
        CREATE INDEX idx_zgloszenia_status ON zgloszenia(status);
    END IF;
END $$;