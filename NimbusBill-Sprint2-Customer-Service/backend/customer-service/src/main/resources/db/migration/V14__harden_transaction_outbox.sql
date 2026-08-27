ALTER TABLE transaction_outbox ADD COLUMN next_attempt_at TIMESTAMPTZ;
ALTER TABLE transaction_outbox ADD COLUMN last_error VARCHAR(1000);
ALTER TABLE transaction_outbox DROP CONSTRAINT chk_transaction_outbox_status;
ALTER TABLE transaction_outbox ADD CONSTRAINT chk_transaction_outbox_status CHECK(status IN ('PENDING','PROCESSING','PUBLISHED','FAILED','DEAD'));
DROP INDEX idx_transaction_outbox_pending;
CREATE INDEX idx_transaction_outbox_ready ON transaction_outbox(status,next_attempt_at,created_at);
