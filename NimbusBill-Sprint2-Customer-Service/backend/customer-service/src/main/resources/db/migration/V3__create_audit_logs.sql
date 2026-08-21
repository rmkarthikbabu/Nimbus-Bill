CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id),
    action VARCHAR(30) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    actor VARCHAR(150) NOT NULL,
    ip_address VARCHAR(64),
    created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_audit_logs_customer_created ON audit_logs(customer_id, created_at DESC);
