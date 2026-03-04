-- V8__create_provinces_table.sql
-- Create provinces table for geographic area management

CREATE TABLE provinces (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,

    -- Bounding Box coordinates (min and max latitude/longitude)
    min_lat DOUBLE PRECISION NOT NULL,
    min_lng DOUBLE PRECISION NOT NULL,
    max_lat DOUBLE PRECISION NOT NULL,
    max_lng DOUBLE PRECISION NOT NULL,

    -- Audit information
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36)
);

-- Create index for code (frequently used in searches)
CREATE INDEX idx_provinces_code ON provinces(code);

-- Create index for name (frequently used in searches)
CREATE INDEX idx_provinces_name ON provinces(name);
