CREATE TABLE product_transaction_types (
    product_id UUID NOT NULL REFERENCES payment_products(id) ON DELETE CASCADE,
    transaction_type VARCHAR(60) NOT NULL,
    PRIMARY KEY (product_id, transaction_type)
);

CREATE TABLE product_currencies (
    product_id UUID NOT NULL REFERENCES payment_products(id) ON DELETE CASCADE,
    currency VARCHAR(3) NOT NULL,
    PRIMARY KEY (product_id, currency)
);

INSERT INTO product_transaction_types(product_id, transaction_type)
SELECT id, product_code FROM payment_products;

INSERT INTO product_currencies(product_id, currency)
SELECT id, CASE WHEN product_code = 'SWIFT' THEN 'USD' ELSE 'INR' END FROM payment_products;

ALTER TABLE notification_outbox ADD COLUMN attempt_count INTEGER NOT NULL DEFAULT 0;
ALTER TABLE notification_outbox ADD COLUMN next_attempt_at TIMESTAMPTZ;
ALTER TABLE notification_outbox ADD COLUMN last_error VARCHAR(1000);
ALTER TABLE notification_outbox ADD COLUMN destination VARCHAR(300);
ALTER TABLE notification_outbox DROP CONSTRAINT chk_notification_status;
ALTER TABLE notification_outbox ADD CONSTRAINT chk_notification_status CHECK(status IN ('PENDING','PROCESSING','SENT','FAILED','DEAD'));
CREATE INDEX idx_notification_retry ON notification_outbox(status,next_attempt_at,created_at);

