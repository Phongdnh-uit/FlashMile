-- V10__create_wards_table.sql

CREATE TABLE wards (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    province_id VARCHAR(36) NOT NULL,
    type VARCHAR(50) NOT NULL DEFAULT 'WARD',
    polygon JSONB,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(36),

    CONSTRAINT uk_wards_code UNIQUE (code),

    CONSTRAINT fk_wards_province FOREIGN KEY (province_id) REFERENCES provinces(id) ON DELETE CASCADE
);

CREATE INDEX idx_wards_code ON wards(code);
CREATE INDEX idx_wards_name ON wards(name);
CREATE INDEX idx_wards_province_id ON wards(province_id);