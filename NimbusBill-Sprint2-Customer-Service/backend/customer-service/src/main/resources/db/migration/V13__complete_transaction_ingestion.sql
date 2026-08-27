ALTER TABLE payment_transactions ADD COLUMN transaction_kind VARCHAR(20) NOT NULL DEFAULT 'PAYMENT';
ALTER TABLE payment_transactions ADD COLUMN ingestion_source VARCHAR(20) NOT NULL DEFAULT 'REST';
ALTER TABLE payment_transactions ADD COLUMN original_transaction_id UUID REFERENCES payment_transactions(id);
ALTER TABLE payment_transactions ADD COLUMN reconciliation_status VARCHAR(20) NOT NULL DEFAULT 'PENDING';
ALTER TABLE payment_transactions ADD COLUMN external_settlement_reference VARCHAR(100);
ALTER TABLE payment_transactions ADD CONSTRAINT chk_transaction_kind CHECK(transaction_kind IN ('PAYMENT','REVERSAL'));
ALTER TABLE payment_transactions ADD CONSTRAINT chk_ingestion_source CHECK(ingestion_source IN ('REST','BATCH','EVENT'));
ALTER TABLE payment_transactions ADD CONSTRAINT chk_reconciliation_status CHECK(reconciliation_status IN ('PENDING','MATCHED','EXCEPTION'));
CREATE UNIQUE INDEX uq_payment_transaction_reversal ON payment_transactions(original_transaction_id) WHERE original_transaction_id IS NOT NULL;

CREATE TABLE transaction_outbox (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL REFERENCES payment_transactions(id),
    event_type VARCHAR(60) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    attempt_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ,
    CONSTRAINT chk_transaction_outbox_status CHECK(status IN ('PENDING','PUBLISHED','FAILED')),
    CONSTRAINT uq_transaction_event UNIQUE(transaction_id,event_type)
);
CREATE INDEX idx_transaction_outbox_pending ON transaction_outbox(status,created_at);
