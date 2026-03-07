-- V11: Create multifactor authentication tables

-- Create the multifactors table
CREATE TABLE multifactors (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    method VARCHAR(20) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE NOT NULL,
    details TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(36),
    updated_by VARCHAR(36)
);

-- Create index for faster lookup by user_id
CREATE INDEX idx_multifactors_user_id ON multifactors(user_id);

-- Create the multifactor_backup_codes table
CREATE TABLE multifactor_backup_codes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    hashed_code VARCHAR(255) NOT NULL,
    used_at TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(36),
    updated_by VARCHAR(36)
);

-- Create index for faster lookup by user_id
CREATE INDEX idx_multifactor_backup_codes_user_id ON multifactor_backup_codes(user_id);

