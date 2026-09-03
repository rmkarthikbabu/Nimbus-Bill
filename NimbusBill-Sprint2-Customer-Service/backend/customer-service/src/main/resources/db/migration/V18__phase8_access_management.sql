CREATE TABLE app_users (
 id UUID PRIMARY KEY, email VARCHAR(255) NOT NULL UNIQUE, display_name VARCHAR(150) NOT NULL,
 role VARCHAR(40) NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
 mfa_required BOOLEAN NOT NULL DEFAULT TRUE, created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
 updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE TABLE user_customer_scopes (
 user_id UUID NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
 customer_id UUID NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
 PRIMARY KEY(user_id, customer_id)
);
CREATE INDEX idx_app_users_status ON app_users(status);
