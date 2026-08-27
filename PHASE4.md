# Phase 4 — Transaction Ingestion

Phase 4 provides REST, batch CSV and event-oriented payment transaction ingestion. A customer/reference/type idempotency key and a customer row lock prevent duplicate processing and duplicate billing under concurrent requests.

## APIs

- `POST /api/v1/transactions` — REST ingestion
- `POST /api/v1/transactions/batch` — independently committed batch items
- `POST /api/v1/transactions/events` — event-adapter ingestion boundary
- `GET /api/v1/transactions` and `GET /api/v1/transactions/{id}` — search/detail
- `GET /api/v1/transactions/summary` — operational KPIs
- `POST /api/v1/transactions/{id}/reversal` — idempotent reversal
- `POST /api/v1/transactions/{id}/reconciliation` — match/exception workflow
- `GET /api/v1/transaction-outbox` and `POST /api/v1/transaction-outbox/{id}/retry` — outbox operations

## External integration boundary

`TransactionOutboxDispatcher` owns retry, exponential backoff, dead-letter transition and operational alerting. Its current local delivery logs the event. Production deployments replace the delivery body with an Amazon SQS, EventBridge or Kafka publisher while retaining the database state machine and retry contract. Credentials and broker endpoints must be supplied by the deployment environment and are intentionally not committed.

## Batch format

```csv
clientReferenceId,productCode,transactionType,amount,currency,sourceAccount,destinationAccount
CLIENT-0001,UPI,UPI,1000.00,INR,ACCOUNT-X,ACCOUNT-Y
```

Quoted values, embedded commas and escaped quotes are supported. Each item commits in a separate transaction and the response reports completed, rejected and failed rows.

## Database

- V12: transaction header, immutable history and one-charge constraint
- V13: reversals, reconciliation, ingestion source and transactional outbox
- V14: retry scheduling, error details and dead-letter support

Run `docker compose up -d --build` in `NimbusBill-Sprint2-Customer-Service` to apply migrations and start PostgreSQL, API and UI.
