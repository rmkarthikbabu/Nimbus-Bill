$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
docker compose -f (Join-Path $root 'NimbusBill-Sprint2-Customer-Service\docker-compose.yml') down
