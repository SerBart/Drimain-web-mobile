-- Add dzial_id column to users table
ALTER TABLE users ADD COLUMN dzial_id BIGINT REFERENCES dzialy(id);

-- Insert ROLE_BIURO if it doesn't exist
INSERT INTO roles (name) VALUES ('ROLE_BIURO') ON CONFLICT (name) DO NOTHING;