CREATE TABLE billing_accounts (
 id UUID PRIMARY KEY, customer_id UUID NOT NULL REFERENCES customers(id), account_code VARCHAR(40) NOT NULL,
 account_name VARCHAR(150) NOT NULL, currency VARCHAR(3) NOT NULL, billing_cycle VARCHAR(20) NOT NULL,
 payment_terms_days INTEGER NOT NULL DEFAULT 30, status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
 created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT uq_billing_account_code UNIQUE(customer_id,account_code),
 CONSTRAINT chk_billing_account_status CHECK(status IN ('ACTIVE','SUSPENDED','CLOSED')),
 CONSTRAINT chk_payment_terms CHECK(payment_terms_days BETWEEN 0 AND 365)
);

CREATE TABLE billing_periods (
 id UUID PRIMARY KEY, billing_account_id UUID NOT NULL REFERENCES billing_accounts(id), period_start DATE NOT NULL,
 period_end DATE NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'OPEN', closed_at TIMESTAMPTZ,
 created_at TIMESTAMPTZ NOT NULL, CONSTRAINT uq_billing_period UNIQUE(billing_account_id,period_start,period_end),
 CONSTRAINT chk_billing_period_dates CHECK(period_end >= period_start),
 CONSTRAINT chk_billing_period_status CHECK(status IN ('OPEN','PROCESSING','CLOSED'))
);

CREATE TABLE billing_runs (
 id UUID PRIMARY KEY, billing_period_id UUID NOT NULL REFERENCES billing_periods(id), status VARCHAR(20) NOT NULL,
 charge_count INTEGER NOT NULL DEFAULT 0, subtotal NUMERIC(19,4) NOT NULL DEFAULT 0,
 tax_total NUMERIC(19,4) NOT NULL DEFAULT 0, grand_total NUMERIC(19,4) NOT NULL DEFAULT 0,
 started_at TIMESTAMPTZ NOT NULL, completed_at TIMESTAMPTZ, failure_reason VARCHAR(500),
 CONSTRAINT chk_billing_run_status CHECK(status IN ('PREVIEWED','PROCESSING','COMPLETED','FAILED'))
);

CREATE TABLE billing_run_items (
 id UUID PRIMARY KEY, billing_run_id UUID NOT NULL REFERENCES billing_runs(id), transaction_charge_id UUID NOT NULL UNIQUE REFERENCES transaction_charges(id),
 transaction_id UUID NOT NULL REFERENCES payment_transactions(id), product_code VARCHAR(30) NOT NULL,
 base_fee NUMERIC(19,4) NOT NULL, tax_amount NUMERIC(19,4) NOT NULL, total_amount NUMERIC(19,4) NOT NULL,
 currency VARCHAR(3) NOT NULL, created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_billing_account_customer ON billing_accounts(customer_id,status);
CREATE INDEX idx_billing_period_account ON billing_periods(billing_account_id,period_start DESC);
CREATE INDEX idx_billing_run_period ON billing_runs(billing_period_id,started_at DESC);
CREATE INDEX idx_billing_run_item_run ON billing_run_items(billing_run_id);
