CREATE SEQUENCE invoice_number_seq START WITH 1;
CREATE TABLE invoices (
 id UUID PRIMARY KEY, billing_run_id UUID NOT NULL UNIQUE REFERENCES billing_runs(id),
 customer_id UUID NOT NULL REFERENCES customers(id), billing_account_id UUID NOT NULL REFERENCES billing_accounts(id),
 invoice_number VARCHAR(40) NOT NULL UNIQUE, status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
 invoice_date DATE NOT NULL, due_date DATE NOT NULL, currency VARCHAR(3) NOT NULL,
 subtotal NUMERIC(19,4) NOT NULL, tax_total NUMERIC(19,4) NOT NULL, adjustment_total NUMERIC(19,4) NOT NULL DEFAULT 0,
 grand_total NUMERIC(19,4) NOT NULL, created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL,
 submitted_at TIMESTAMPTZ, approved_at TIMESTAMPTZ, issued_at TIMESTAMPTZ,
 CONSTRAINT chk_invoice_status CHECK(status IN ('DRAFT','SUBMITTED','APPROVED','ISSUED','CANCELLED'))
);
CREATE TABLE invoice_line_items (
 id UUID PRIMARY KEY, invoice_id UUID NOT NULL REFERENCES invoices(id), billing_run_item_id UUID NOT NULL UNIQUE REFERENCES billing_run_items(id),
 transaction_id UUID NOT NULL, product_code VARCHAR(30) NOT NULL, description VARCHAR(200) NOT NULL,
 base_amount NUMERIC(19,4) NOT NULL, tax_amount NUMERIC(19,4) NOT NULL, total_amount NUMERIC(19,4) NOT NULL,
 currency VARCHAR(3) NOT NULL, created_at TIMESTAMPTZ NOT NULL
);
CREATE TABLE invoice_status_history (
 id UUID PRIMARY KEY, invoice_id UUID NOT NULL REFERENCES invoices(id), from_status VARCHAR(20),
 to_status VARCHAR(20) NOT NULL, actor VARCHAR(100) NOT NULL, comment VARCHAR(500), created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_invoice_customer ON invoices(customer_id,invoice_date DESC);
CREATE INDEX idx_invoice_status ON invoices(status,due_date);
CREATE INDEX idx_invoice_line_invoice ON invoice_line_items(invoice_id);
CREATE INDEX idx_invoice_history_invoice ON invoice_status_history(invoice_id,created_at);
