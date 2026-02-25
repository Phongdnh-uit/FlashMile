-- V7: Create permission model and add type to roles table
ALTER TABLE roles ADD COLUMN type VARCHAR(20) DEFAULT 'CUSTOM' NOT NULL;

CREATE TABLE permissions (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(100) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(36),
    updated_by VARCHAR(36)
);

CREATE TABLE role_permissions (
    PRIMARY KEY (role_id, permission_id),
    role_id VARCHAR(36) NOT NULL,
    permission_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_role
        FOREIGN KEY(role_id)
        REFERENCES roles(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_permission
        FOREIGN KEY(permission_id)
        REFERENCES permissions(id)
        ON DELETE CASCADE
);
