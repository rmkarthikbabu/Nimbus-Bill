ALTER TABLE customer_products ADD COLUMN billing_account_id UUID;
ALTER TABLE customer_products ADD COLUMN transaction_limit NUMERIC(19,4);
ALTER TABLE customer_products ADD COLUMN daily_limit NUMERIC(19,4);
ALTER TABLE customer_pricing_assignments ADD COLUMN billing_account_id UUID;
ALTER TABLE customer_pricing_assignments ADD COLUMN product_id UUID REFERENCES payment_products(id);
ALTER TABLE customer_pricing_assignments ADD COLUMN superseded_by UUID REFERENCES customer_pricing_assignments(id);
CREATE INDEX idx_customer_pricing_scope ON customer_pricing_assignments(customer_id,product_id,billing_account_id,effective_from);
