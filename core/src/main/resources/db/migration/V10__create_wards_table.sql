-- V10__create_wards_table.sql

CREATE TABLE wards (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    province_id VARCHAR(36) NOT NULL,

    -- Bounding Box coordinates (min and max latitude/longitude)
    min_lat DOUBLE PRECISION NOT NULL,
    min_lng DOUBLE PRECISION NOT NULL,
    max_lat DOUBLE PRECISION NOT NULL,
    max_lng DOUBLE PRECISION NOT NULL,

    -- Audit information
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36),

    -- Foreign key constraint to provinces table
    CONSTRAINT fk_wards_province FOREIGN KEY (province_id) REFERENCES provinces(id) ON DELETE CASCADE
);