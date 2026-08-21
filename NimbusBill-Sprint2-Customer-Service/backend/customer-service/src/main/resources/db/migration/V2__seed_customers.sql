INSERT INTO customers (id, customer_code, customer_name, legal_name, customer_type, industry, country, currency, billing_cycle, status, tax_identifier, website, created_at, updated_at, version)
VALUES
('11111111-1111-1111-1111-111111111111','ACME_IN','Acme Payments India','Acme Payments India Private Limited','CORPORATE','Financial Services','IN','INR','MONTHLY','ACTIVE','29ABCDE1234F1Z5','https://example.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0),
('22222222-2222-2222-2222-222222222222','NORTHSTAR','Northstar Retail Bank','Northstar Retail Bank Limited','BANK','Banking','GB','GBP','MONTHLY','PENDING',NULL,'https://example.org',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
