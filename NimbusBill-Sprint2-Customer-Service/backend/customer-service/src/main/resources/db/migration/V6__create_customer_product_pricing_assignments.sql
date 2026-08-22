CREATE TABLE customer_products (
 id UUID PRIMARY KEY, customer_id UUID NOT NULL REFERENCES customers(id), product_id UUID NOT NULL REFERENCES payment_products(id),
 enabled BOOLEAN NOT NULL, activation_date DATE NOT NULL, expiry_date DATE, created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT uq_customer_product UNIQUE(customer_id,product_id), CONSTRAINT chk_customer_product_dates CHECK(expiry_date IS NULL OR expiry_date >= activation_date)
);
CREATE TABLE customer_pricing_assignments (
 id UUID PRIMARY KEY, customer_id UUID NOT NULL REFERENCES customers(id), plan_id UUID NOT NULL REFERENCES pricing_plans(id),
 effective_from DATE NOT NULL, effective_to DATE, active BOOLEAN NOT NULL, created_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT chk_customer_pricing_dates CHECK(effective_to IS NULL OR effective_to >= effective_from)
);
CREATE INDEX idx_customer_products_customer ON customer_products(customer_id);
CREATE INDEX idx_customer_pricing_customer_active ON customer_pricing_assignments(customer_id,active);
