ALTER TABLE pricing_plan_versions ADD COLUMN submitted_by VARCHAR(150);
ALTER TABLE pricing_plan_versions ADD COLUMN submitted_at TIMESTAMPTZ;
ALTER TABLE pricing_plan_versions ADD COLUMN decision_comment VARCHAR(500);

CREATE TABLE pricing_audit_logs (
 id UUID PRIMARY KEY, entity_type VARCHAR(40) NOT NULL, entity_id UUID NOT NULL,
 action VARCHAR(40) NOT NULL, actor VARCHAR(150) NOT NULL, actor_role VARCHAR(60),
 old_value TEXT, new_value TEXT, comments VARCHAR(500), ip_address VARCHAR(64),
 created_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX idx_pricing_audit_entity ON pricing_audit_logs(entity_type,entity_id,created_at DESC);

CREATE TABLE notification_outbox (
 id UUID PRIMARY KEY, event_type VARCHAR(60) NOT NULL, entity_type VARCHAR(40) NOT NULL,
 entity_id UUID NOT NULL, recipient_role VARCHAR(60) NOT NULL, subject VARCHAR(200) NOT NULL,
 message VARCHAR(1000) NOT NULL, status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
 created_at TIMESTAMPTZ NOT NULL, sent_at TIMESTAMPTZ,
 CONSTRAINT chk_notification_status CHECK(status IN ('PENDING','SENT','FAILED'))
);
CREATE INDEX idx_notification_outbox_status ON notification_outbox(status,created_at);
