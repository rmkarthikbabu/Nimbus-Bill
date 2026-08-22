CREATE TABLE pricing_plans (
 id UUID PRIMARY KEY, plan_code VARCHAR(30) NOT NULL UNIQUE, plan_name VARCHAR(120) NOT NULL,
 description VARCHAR(500), created_at TIMESTAMPTZ NOT NULL, updated_at TIMESTAMPTZ NOT NULL, version BIGINT NOT NULL DEFAULT 0
);
CREATE TABLE pricing_plan_versions (
 id UUID PRIMARY KEY, plan_id UUID NOT NULL REFERENCES pricing_plans(id), version_number INT NOT NULL,
 currency VARCHAR(3) NOT NULL, effective_from DATE NOT NULL, effective_to DATE, status VARCHAR(30) NOT NULL,
 created_by VARCHAR(150) NOT NULL, approved_by VARCHAR(150), created_at TIMESTAMPTZ NOT NULL, approved_at TIMESTAMPTZ,
 CONSTRAINT uq_plan_version UNIQUE(plan_id,version_number),
 CONSTRAINT chk_pricing_version_status CHECK(status IN ('DRAFT','PENDING_APPROVAL','APPROVED','ACTIVE','REJECTED','EXPIRED')),
 CONSTRAINT chk_pricing_version_dates CHECK(effective_to IS NULL OR effective_to >= effective_from)
);
CREATE TABLE pricing_rules (
 id UUID PRIMARY KEY, version_id UUID NOT NULL REFERENCES pricing_plan_versions(id) ON DELETE CASCADE,
 product_id UUID NOT NULL REFERENCES payment_products(id), charge_type VARCHAR(20) NOT NULL,
 fixed_fee NUMERIC(19,4), percentage_rate NUMERIC(12,6), minimum_fee NUMERIC(19,4), maximum_fee NUMERIC(19,4),
 tax_rate NUMERIC(8,4) NOT NULL DEFAULT 0, priority INT NOT NULL DEFAULT 100,
 CONSTRAINT chk_rule_charge_type CHECK(charge_type IN ('FIXED','PERCENTAGE','SLAB','HYBRID')),
 CONSTRAINT chk_rule_values CHECK(fixed_fee IS NULL OR fixed_fee >= 0),
 CONSTRAINT chk_rule_percentage CHECK(percentage_rate IS NULL OR percentage_rate >= 0),
 CONSTRAINT chk_rule_fee_range CHECK(minimum_fee IS NULL OR maximum_fee IS NULL OR minimum_fee <= maximum_fee)
);
CREATE TABLE pricing_slabs (
 id UUID PRIMARY KEY, rule_id UUID NOT NULL REFERENCES pricing_rules(id) ON DELETE CASCADE,
 lower_bound NUMERIC(19,4) NOT NULL, upper_bound NUMERIC(19,4), flat_fee NUMERIC(19,4), percentage_rate NUMERIC(12,6), sequence_no INT NOT NULL,
 CONSTRAINT uq_rule_slab_sequence UNIQUE(rule_id,sequence_no),
 CONSTRAINT chk_slab_range CHECK(upper_bound IS NULL OR upper_bound > lower_bound)
);
CREATE INDEX idx_pricing_versions_plan_status ON pricing_plan_versions(plan_id,status);
CREATE INDEX idx_pricing_rules_version_product ON pricing_rules(version_id,product_id);
