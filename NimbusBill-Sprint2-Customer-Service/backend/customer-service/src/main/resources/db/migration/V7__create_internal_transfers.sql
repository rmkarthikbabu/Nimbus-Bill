CREATE TABLE transfer_accounts (
 id UUID PRIMARY KEY, customer_id UUID REFERENCES customers(id), account_number VARCHAR(80) NOT NULL UNIQUE,
 account_name VARCHAR(150) NOT NULL, account_type VARCHAR(20) NOT NULL, currency VARCHAR(3) NOT NULL,
 ledger_balance NUMERIC(19,4) NOT NULL DEFAULT 0, available_balance NUMERIC(19,4) NOT NULL DEFAULT 0,
 status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE', version BIGINT NOT NULL DEFAULT 0,
 created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT chk_transfer_account_type CHECK(account_type IN ('CLIENT','INTERNAL')),
 CONSTRAINT chk_transfer_account_status CHECK(status IN ('ACTIVE','BLOCKED','CLOSED')),
 CONSTRAINT chk_transfer_account_balances CHECK(ledger_balance >= 0 AND available_balance >= 0)
);

CREATE TABLE transfer_transactions (
 id UUID PRIMARY KEY, client_reference_id VARCHAR(100) NOT NULL, customer_id UUID NOT NULL REFERENCES customers(id),
 source_account_id UUID NOT NULL REFERENCES transfer_accounts(id), destination_account_id UUID NOT NULL REFERENCES transfer_accounts(id),
 source_bridge_account_id UUID NOT NULL REFERENCES transfer_accounts(id), destination_bridge_account_id UUID NOT NULL REFERENCES transfer_accounts(id),
 product_code VARCHAR(30) NOT NULL, amount NUMERIC(19,4) NOT NULL, currency VARCHAR(3) NOT NULL,
 status VARCHAR(30) NOT NULL, current_step SMALLINT NOT NULL DEFAULT 0, billing_status VARCHAR(20) NOT NULL DEFAULT 'NOT_BILLED',
 pricing_plan_version_id UUID REFERENCES pricing_plan_versions(id), failure_code VARCHAR(80),
 created_at TIMESTAMPTZ NOT NULL, completed_at TIMESTAMPTZ, version BIGINT NOT NULL DEFAULT 0,
 CONSTRAINT uq_transfer_client_reference UNIQUE(customer_id,client_reference_id),
 CONSTRAINT chk_transfer_amount CHECK(amount > 0),
 CONSTRAINT chk_transfer_status CHECK(status IN ('RECEIVED','FUNDS_CHECKED','IN_PROGRESS','COMPLETED','FAILED','REVERSING','REVERSED')),
 CONSTRAINT chk_transfer_billing_status CHECK(billing_status IN ('NOT_BILLED','BILLED','REVERSED'))
);

CREATE TABLE fund_reservations (
 id UUID PRIMARY KEY, transfer_id UUID NOT NULL UNIQUE REFERENCES transfer_transactions(id), account_id UUID NOT NULL REFERENCES transfer_accounts(id),
 amount NUMERIC(19,4) NOT NULL, status VARCHAR(20) NOT NULL, expires_at TIMESTAMPTZ NOT NULL, created_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT chk_reservation_status CHECK(status IN ('ACTIVE','CONSUMED','RELEASED','EXPIRED'))
);

CREATE TABLE transfer_legs (
 id UUID PRIMARY KEY, transfer_id UUID NOT NULL REFERENCES transfer_transactions(id), leg_sequence SMALLINT NOT NULL,
 leg_type VARCHAR(40) NOT NULL, debit_account_id UUID NOT NULL REFERENCES transfer_accounts(id), credit_account_id UUID NOT NULL REFERENCES transfer_accounts(id),
 amount NUMERIC(19,4) NOT NULL, currency VARCHAR(3) NOT NULL, status VARCHAR(20) NOT NULL,
 ledger_reference VARCHAR(100) UNIQUE, posted_at TIMESTAMPTZ, failure_code VARCHAR(80),
 CONSTRAINT uq_transfer_leg_sequence UNIQUE(transfer_id,leg_sequence),
 CONSTRAINT chk_transfer_leg_sequence CHECK(leg_sequence BETWEEN 1 AND 3),
 CONSTRAINT chk_transfer_leg_status CHECK(status IN ('PENDING','POSTED','FAILED','REVERSED'))
);

CREATE TABLE ledger_transactions (
 id UUID PRIMARY KEY, transfer_id UUID NOT NULL REFERENCES transfer_transactions(id), leg_id UUID NOT NULL UNIQUE REFERENCES transfer_legs(id),
 transaction_type VARCHAR(40) NOT NULL, status VARCHAR(20) NOT NULL, posted_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE ledger_entries (
 id UUID PRIMARY KEY, ledger_transaction_id UUID NOT NULL REFERENCES ledger_transactions(id), account_id UUID NOT NULL REFERENCES transfer_accounts(id),
 entry_type VARCHAR(10) NOT NULL, amount NUMERIC(19,4) NOT NULL, currency VARCHAR(3) NOT NULL, created_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT chk_ledger_entry_type CHECK(entry_type IN ('DEBIT','CREDIT')),
 CONSTRAINT chk_ledger_entry_amount CHECK(amount > 0)
);

CREATE TABLE billable_events (
 id UUID PRIMARY KEY, transfer_id UUID NOT NULL REFERENCES transfer_transactions(id), customer_id UUID NOT NULL REFERENCES customers(id),
 event_type VARCHAR(50) NOT NULL, pricing_plan_version_id UUID NOT NULL REFERENCES pricing_plan_versions(id),
 base_fee NUMERIC(19,4) NOT NULL, tax_amount NUMERIC(19,4) NOT NULL, charge_amount NUMERIC(19,4) NOT NULL,
 currency VARCHAR(3) NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'PENDING', created_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT uq_billable_transfer_event UNIQUE(transfer_id,event_type)
);

CREATE INDEX idx_transfer_customer_created ON transfer_transactions(customer_id,created_at DESC);
CREATE INDEX idx_transfer_status ON transfer_transactions(status);
CREATE INDEX idx_transfer_legs_transfer ON transfer_legs(transfer_id,leg_sequence);
CREATE INDEX idx_ledger_entries_account ON ledger_entries(account_id,created_at);
CREATE INDEX idx_billable_events_status ON billable_events(status,created_at);
