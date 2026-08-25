# NimbusBill

NimbusBill is a customer-management and billing platform MVP built with React, TypeScript, Material UI, Spring Boot 4, Java 21, and PostgreSQL.

## Current capabilities

- Customer creation, search, filtering, viewing, and editing
- Activate, suspend, and soft-delete lifecycle operations
- PostgreSQL persistence with Flyway migrations
- Customer audit history
- Role-based access-control foundation compatible with Amazon Cognito JWTs
- Docker Compose development environment
- Phase 3 payment-product catalogue with transaction types and currencies
- Versioned fixed, percentage, hybrid, and slab pricing with min/max fees and taxes
- Maker-checker approvals, effective dating, customer overrides, preview, audit, and notification outbox delivery
- Customer product limits and scoped customer/product/billing-account pricing assignments with rollback history
- Idempotent internal transfers with three ledger legs and one billable event

## Start the complete stack

Requirements: Docker Desktop and PowerShell.

```powershell
.\start-dev.ps1
```

Open <http://localhost:3000>. To stop the stack:

```powershell
.\stop-dev.ps1
```

## Development

The backend is under `NimbusBill-Sprint2-Customer-Service/backend/customer-service` and the frontend is under `NimbusBill-Customer-Portal-React/nimbusbill-ui`.

Local Docker credentials are development-only. Configure database credentials and the Cognito issuer through environment variables in deployed environments.

## Verification

```powershell
cd NimbusBill-Sprint2-Customer-Service/backend/customer-service
mvn test

cd ../../../NimbusBill-Customer-Portal-React/nimbusbill-ui
npm test
npm run build
```

Phase 3 database changes are applied automatically through Flyway migration `V11__complete_phase3.sql`.
