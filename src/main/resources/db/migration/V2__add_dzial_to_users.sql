-- Add dzial_id column to users table
-- Only create if not exists to avoid collisions
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'users' 
        AND column_name = 'dzial_id'
    ) THEN
        ALTER TABLE users ADD COLUMN dzial_id BIGINT;
        ALTER TABLE users ADD CONSTRAINT fk_users_dzial 
            FOREIGN KEY (dzial_id) REFERENCES dzialy(id);
        CREATE INDEX idx_users_dzial_id ON users(dzial_id);
    END IF;
END $$;