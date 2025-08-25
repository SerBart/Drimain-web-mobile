# DriMain API Client

A TypeScript client library for the DriMain API, providing type-safe access to all endpoints.

## Installation

This package is part of the DriMain monorepo and should be built and used locally.

## Usage

### Basic Setup

```typescript
import { createApiClient } from '@drimain/api-client';

// Create client without authentication
const client = createApiClient('http://localhost:8080');

// Or with authentication token
const client = createApiClient('http://localhost:8080', 'your-jwt-token');
```

### Authentication

```typescript
// Login
const authResponse = await client.auth.login({
    username: 'admin',
    password: 'password'
});

// Set the token for future requests
client.setToken(authResponse.token);

// Get current user info
const userInfo = await client.auth.me();
```

### Reports (Raporty)

```typescript
// List reports with pagination
const reports = await client.raporty.list({
    page: 0,
    size: 20,
    status: RaportStatus.NOWY
});

// Create new report
const newReport = await client.raporty.create({
    typNaprawy: 'Engine repair',
    dataNaprawy: '2024-01-15',
    opis: 'Replace belt'
});

// Update report
const updatedReport = await client.raporty.update(1, {
    status: RaportStatus.ZAKONCZONE
});
```

### Parts (Części)

```typescript
// List all parts
const parts = await client.czesci.list();

// Search parts
const searchResults = await client.czesci.list({
    q: 'pasek',
    belowMin: true
});

// Adjust quantity
await client.czesci.adjustQuantity(1, { delta: -2 });
```

### Issues (Zgłoszenia)

```typescript
// List issues
const issues = await client.zgloszenia.list({
    status: ZgloszenieStatus.OPEN
});

// Create new issue
const newIssue = await client.zgloszenia.create({
    typ: 'Machine failure',
    imie: 'Jan',
    nazwisko: 'Kowalski',
    opis: 'Machine won\'t start'
});
```

## API Documentation

For complete API documentation, see the OpenAPI specification in `docs/openapi.yaml`.

## Development

To build the client:

```bash
cd packages/api-client
npm run build
```

## Types

All TypeScript interfaces and enums are exported from the main package and can be imported directly:

```typescript
import { 
    RaportStatus, 
    ZgloszenieStatus, 
    Raport, 
    Part, 
    Zgloszenie 
} from '@drimain/api-client';
```