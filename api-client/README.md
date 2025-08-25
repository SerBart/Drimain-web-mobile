# API Client

This directory contains the generated API client for the DriMain application.

## Usage

The API client is generated from the OpenAPI specification located in `../docs/openapi.yaml`.

To regenerate the client:

```bash
npm run gen:api
```

## Structure

- `index.ts` - Main export file
- `models/` - Generated data models and types
- `services/` - Generated API service classes

## Configuration

The client requires configuration with the base URL and authentication:

```typescript
import { ApiClientConfig } from './index';

const config: ApiClientConfig = {
  BASE: 'http://localhost:8080',
  TOKEN: 'your-jwt-token-here'
};
```

## Note

This is a minimal placeholder structure. Run `npm install` and `npm run gen:api` to generate the full client with all models and services.