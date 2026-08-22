CREATE TABLE payment_products (
    id UUID PRIMARY KEY,
    product_code VARCHAR(30) NOT NULL UNIQUE,
    product_name VARCHAR(120) NOT NULL,
    description VARCHAR(500),
    pricing_unit VARCHAR(30) NOT NULL,
    minimum_fee NUMERIC(19,4),
    maximum_fee NUMERIC(19,4),
    tax_applicable BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(20) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT chk_product_pricing_unit CHECK (pricing_unit IN ('FIXED','PERCENTAGE','SLAB','HYBRID')),
    CONSTRAINT chk_product_status CHECK (status IN ('DRAFT','ACTIVE','INACTIVE')),
    CONSTRAINT chk_product_fee_range CHECK (minimum_fee IS NULL OR maximum_fee IS NULL OR minimum_fee <= maximum_fee),
    CONSTRAINT chk_product_dates CHECK (effective_to IS NULL OR effective_to >= effective_from)
);
CREATE INDEX idx_payment_products_status ON payment_products(status);
CREATE INDEX idx_payment_products_name ON payment_products(product_name);

INSERT INTO payment_products (id,product_code,product_name,description,pricing_unit,tax_applicable,status,effective_from,created_at,updated_at,version) VALUES
('31000000-0000-0000-0000-000000000001','UPI','UPI','Unified Payments Interface transactions','FIXED',TRUE,'ACTIVE',CURRENT_DATE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0),
('31000000-0000-0000-0000-000000000002','NEFT','NEFT','National Electronic Funds Transfer','SLAB',TRUE,'ACTIVE',CURRENT_DATE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0),
('31000000-0000-0000-0000-000000000003','RTGS','RTGS','Real Time Gross Settlement','SLAB',TRUE,'ACTIVE',CURRENT_DATE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0),
('31000000-0000-0000-0000-000000000004','IMPS','IMPS','Immediate Payment Service','FIXED',TRUE,'ACTIVE',CURRENT_DATE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0),
('31000000-0000-0000-0000-000000000005','CARDS','Cards','Debit and credit card transactions','PERCENTAGE',TRUE,'ACTIVE',CURRENT_DATE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0),
('31000000-0000-0000-0000-000000000006','SWIFT','SWIFT','International SWIFT transfers','HYBRID',TRUE,'ACTIVE',CURRENT_DATE,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
