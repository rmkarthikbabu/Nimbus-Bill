CREATE TABLE customers (
    id UUID PRIMARY KEY,
    customer_code VARCHAR(30) NOT NULL UNIQUE,
    customer_name VARCHAR(150) NOT NULL,
    legal_name VARCHAR(180) NOT NULL,
    customer_type VARCHAR(40) NOT NULL,
    industry VARCHAR(80),
    country VARCHAR(2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    billing_cycle VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    tax_identifier VARCHAR(50),
    website VARCHAR(200),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT chk_customer_status CHECK (status IN ('PENDING','ACTIVE','SUSPENDED','INACTIVE')),
    CONSTRAINT chk_billing_cycle CHECK (billing_cycle IN ('WEEKLY','MONTHLY','QUARTERLY'))
);
CREATE INDEX idx_customers_name ON customers(customer_name);
CREATE INDEX idx_customers_status ON customers(status);
