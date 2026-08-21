# NimbusBill

NimbusBill is a customer-management and billing platform MVP built with React, TypeScript, Material UI, Spring Boot 4, Java 21, and PostgreSQL.

## Current capabilities

- Customer creation, search, filtering, viewing, and editing
- Activate, suspend, and soft-delete lifecycle operations
- PostgreSQL persistence with Flyway migrations
- Customer audit history
- Role-based access-control foundation compatible with Amazon Cognito JWTs
- Docker Compose development environment

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
