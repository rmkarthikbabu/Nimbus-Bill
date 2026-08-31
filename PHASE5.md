# Phase 5 — Billing Engine Foundation

Phase 5 introduces the first production-oriented billing workflow on top of the
customer, pricing, transaction-ingestion, and charge data delivered in earlier
phases.

## Completed scope

- Customer billing accounts with currency and billing-cycle configuration.
- Billing periods with controlled date ranges and lifecycle statuses.
- Billing-run preview using pending transaction charges for the selected
  customer, currency, and period.
- Billing-run execution that creates immutable run items and marks source
  charges as billed.
- Exactly-once billing protection through a unique transaction-charge reference
  on billing-run items and database locking during execution.
- Billing Operations UI for account setup, period setup, preview, execution, and
  run-history review.
- PostgreSQL Flyway migration, REST APIs, automated backend tests, frontend
  tests, and production frontend build verification.

## REST endpoints

- `POST /api/v1/billing-accounts`
- `GET /api/v1/billing-accounts`
- `POST /api/v1/billing-periods`
- `GET /api/v1/billing-periods`
- `POST /api/v1/billing-periods/{id}/preview`
- `POST /api/v1/billing-runs/{id}/execute`
- `GET /api/v1/billing-runs`

## Next phase

Phase 6 can build invoice generation, tax calculation, invoice documents,
approval workflows, adjustments, and delivery on this billing foundation.
