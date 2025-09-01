-- Add attachments table for Zgloszenie file attachments

CREATE TABLE attachments (
    id BIGSERIAL PRIMARY KEY,
    zgloszenie_id BIGINT NOT NULL,
    original_filename VARCHAR(500) NOT NULL,
    stored_filename VARCHAR(500) UNIQUE NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    checksum VARCHAR(64),
    CONSTRAINT fk_attachment_zgloszenie 
        FOREIGN KEY (zgloszenie_id) 
        REFERENCES zgloszenie(id) 
        ON DELETE CASCADE
);

-- Create index on zgloszenie_id for performance
CREATE INDEX idx_attachment_zgloszenie ON attachments(zgloszenie_id);

-- Create index on stored_filename for fast lookups
CREATE INDEX idx_attachment_stored_filename ON attachments(stored_filename);