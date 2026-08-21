# NimbusBill UI

Figma-quality React + TypeScript + Material UI reference implementation for a cloud-native payment billing customer portal.

## Included screens
- Responsive application shell
- Executive dashboard
- Customer list with filters
- Customer profile
- Persisted customer onboarding and edit form
- Customer lifecycle and audit history
- Placeholder routes for pricing, products, invoices, reports, administration, and settings

## Run locally
```bash
npm install
npm run dev
```

The development server proxies `/api` to `http://localhost:8080`.

## Run the complete stack
From the repository root:
```powershell
.\start-dev.ps1
```
Open `http://localhost:3000`. Stop it with `.\stop-dev.ps1`.

Set `SECURITY_ENABLED=true` and configure Spring Security's JWT issuer URI for Amazon Cognito in secured environments. Cognito groups map to the application roles.

## Build
```bash
npm run build
```
