-- V3__create_recipient_contacts_table.sql

CREATE TABLE recipient_contacts (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(500),
    note VARCHAR(1000),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by VARCHAR(36),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_by VARCHAR(36),

    CONSTRAINT uk_recipient_user_phone UNIQUE (user_id, phone_number),
    CONSTRAINT fk_recipient_contacts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);