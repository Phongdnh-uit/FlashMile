CREATE TABLE linked_accounts (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    provider_user_id VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(36),
    updated_by VARCHAR(36)
);

-- Prevent duplicate OAuth mapping
ALTER TABLE linked_accounts
ADD CONSTRAINT uk_linked_accounts_provider_user
UNIQUE (provider, provider_user_id);

-- Index for fast lookup by user
CREATE INDEX idx_linked_accounts_user_id
ON linked_accounts (user_id);

-- Index for provider lookup
CREATE INDEX idx_linked_accounts_provider
ON linked_accounts (provider);
