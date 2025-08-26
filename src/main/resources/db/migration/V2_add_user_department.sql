-- Add department (dzial) foreign key to users table
ALTER TABLE users ADD COLUMN dzial_id BIGINT;

-- Add foreign key constraint to dzialy table
ALTER TABLE users ADD CONSTRAINT fk_users_dzial 
    FOREIGN KEY (dzial_id) REFERENCES dzialy(id);

-- Make dzialy.nazwa unique if it's not already
ALTER TABLE dzialy ADD CONSTRAINT uk_dzialy_nazwa UNIQUE (nazwa);