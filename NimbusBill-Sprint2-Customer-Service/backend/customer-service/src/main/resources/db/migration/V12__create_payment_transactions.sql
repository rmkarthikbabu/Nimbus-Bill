CREATE TABLE payment_transactions (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL REFERENCES customers(id),
    client_reference_id VARCHAR(100) NOT NULL,
    product_code VARCHAR(30) NOT NULL,
    transaction_type VARCHAR(60) NOT NULL,
    amount NUMERIC(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    source_account VARCHAR(100),
    destination_account VARCHAR(100),
    status VARCHAR(30) NOT NULL,
    failure_reason VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    completed_at TIMESTAMPTZ,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uq_payment_transaction_reference UNIQUE(customer_id, client_reference_id, transaction_type),
    CONSTRAINT chk_payment_transaction_amount CHECK(amount > 0),
    CONSTRAINT chk_payment_transaction_status CHECK(status IN ('RECEIVED','VALIDATING','ACCEPTED','PROCESSING','COMPLETED','REJECTED','FAILED'))
);

CREATE TABLE transaction_status_history (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL REFERENCES payment_transactions(id) ON DELETE CASCADE,
    status VARCHAR(30) NOT NULL,
    detail VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE transaction_charges (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL UNIQUE REFERENCES payment_transactions(id),
    customer_id UUID NOT NULL REFERENCES customers(id),
    pricing_plan_version_id UUID NOT NULL REFERENCES pricing_plan_versions(id),
    base_fee NUMERIC(19,4) NOT NULL,
    tax_amount NUMERIC(19,4) NOT NULL,
    charge_amount NUMERIC(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT chk_transaction_charge_status CHECK(status IN ('PENDING','BILLED','REVERSED'))
);

CREATE INDEX idx_payment_transaction_customer_created ON payment_transactions(customer_id, created_at DESC);
CREATE INDEX idx_payment_transaction_status ON payment_transactions(status, created_at DESC);
CREATE INDEX idx_transaction_history_transaction ON transaction_status_history(transaction_id, created_at);
CREATE INDEX idx_transaction_charge_status ON transaction_charges(status, created_at);
