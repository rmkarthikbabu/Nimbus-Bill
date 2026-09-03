CREATE TABLE ai_query_audits (
 id UUID PRIMARY KEY, actor VARCHAR(255) NOT NULL, customer_id UUID REFERENCES customers(id),
 question VARCHAR(1000) NOT NULL, answer_summary TEXT NOT NULL, intent VARCHAR(60) NOT NULL,
 created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX idx_ai_query_audits_actor_created ON ai_query_audits(actor, created_at DESC);
