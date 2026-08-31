ALTER TABLE invoices DROP CONSTRAINT chk_invoice_status;
ALTER TABLE invoices ADD CONSTRAINT chk_invoice_status CHECK(status IN ('DRAFT','SUBMITTED','APPROVED','REJECTED','ISSUED','PAID','OVERDUE','DISPUTED','CANCELLED'));
CREATE TABLE customer_tax_profiles (
 id UUID PRIMARY KEY, customer_id UUID NOT NULL UNIQUE REFERENCES customers(id), tax_identifier VARCHAR(40),
 tax_country VARCHAR(2) NOT NULL, tax_region VARCHAR(80), place_of_supply VARCHAR(80),
 default_tax_rate NUMERIC(8,4) NOT NULL DEFAULT 0, tax_exempt BOOLEAN NOT NULL DEFAULT FALSE,
 effective_from DATE NOT NULL, created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL
);
CREATE TABLE invoice_adjustments (
 id UUID PRIMARY KEY, invoice_id UUID NOT NULL REFERENCES invoices(id), adjustment_type VARCHAR(10) NOT NULL,
 amount NUMERIC(19,4) NOT NULL, reason VARCHAR(300) NOT NULL, created_by VARCHAR(100) NOT NULL,
 created_at TIMESTAMPTZ NOT NULL, CONSTRAINT chk_adjustment_type CHECK(adjustment_type IN ('CREDIT','DEBIT')),
 CONSTRAINT chk_adjustment_amount CHECK(amount > 0)
);
CREATE TABLE invoice_deliveries (
 id UUID PRIMARY KEY, invoice_id UUID NOT NULL REFERENCES invoices(id), channel VARCHAR(20) NOT NULL,
 destination VARCHAR(255) NOT NULL, status VARCHAR(20) NOT NULL, attempt_count INTEGER NOT NULL DEFAULT 1,
 last_error VARCHAR(500), created_at TIMESTAMPTZ NOT NULL, delivered_at TIMESTAMPTZ,
 CONSTRAINT chk_delivery_channel CHECK(channel IN ('EMAIL','WEBHOOK','API')),
 CONSTRAINT chk_delivery_status CHECK(status IN ('PENDING','DELIVERED','FAILED'))
);
CREATE INDEX idx_adjustment_invoice ON invoice_adjustments(invoice_id);
CREATE INDEX idx_delivery_invoice ON invoice_deliveries(invoice_id,created_at DESC);
