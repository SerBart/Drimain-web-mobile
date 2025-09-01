-- Attachment table for file attachments to zgloszenia (issues)
CREATE TABLE attachments (
    id BIGSERIAL PRIMARY KEY,
    zgloszenie_id BIGINT NOT NULL REFERENCES zgloszenia(id) ON DELETE CASCADE,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    checksum VARCHAR(128),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(120),
    CONSTRAINT uk_attachments_stored_filename UNIQUE (stored_filename)
);

-- Index for faster lookups by zgloszenie_id
CREATE INDEX idx_attachments_zgloszenie_id ON attachments(zgloszenie_id);

-- Add missing columns to zgloszenia table for proper auditing
ALTER TABLE zgloszenia 
ADD COLUMN IF NOT EXISTS created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN IF NOT EXISTS dzial_id BIGINT REFERENCES dzialy(id),
ADD COLUMN IF NOT EXISTS autor_id BIGINT REFERENCES users(id),
ADD COLUMN IF NOT EXISTS tytul VARCHAR(255);