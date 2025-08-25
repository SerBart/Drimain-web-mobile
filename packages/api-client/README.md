# DriMain API Client

TypeScript client for the DriMain API, generated from OpenAPI specification.

## Installation

This client is generated from the OpenAPI specification and is meant to be used within the DriMain project.

## Usage

### Basic Setup

```typescript
import { DriMainApiClient, OpenAPI } from '@drimain/api-client';

// Configure the API client
OpenAPI.BASE = 'http://localhost:8080';
OpenAPI.TOKEN = 'your-jwt-token-here';

// Create client instance
const client = new DriMainApiClient();
```

### Authentication

```typescript
import { AuthenticationService } from '@drimain/api-client';

// Login
const authResponse = await AuthenticationService.login({
  requestBody: {
    username: 'your-username',
    password: 'your-password'
  }
});

// Set token for subsequent requests
OpenAPI.TOKEN = authResponse.token;

// Get current user info
const userInfo = await AuthenticationService.getCurrentUser();
console.log('Current user:', userInfo.username, 'Roles:', userInfo.roles);
```

### Working with Reports

```typescript
import { ReportsService } from '@drimain/api-client';

// List reports with pagination and filtering
const reportsPage = await ReportsService.listRaporty({
  status: 'NOWY',
  page: 0,
  size: 10,
  sort: 'dataNaprawy,desc'
});

console.log('Total reports:', reportsPage.totalElements);
console.log('Reports:', reportsPage.content);

// Create a new report
const newReport = await ReportsService.createRaport({
  requestBody: {
    typNaprawy: 'Naprawa silnika',
    opis: 'Wymiana uszczelki',
    maszynaId: 1,
    osobaId: 1,
    dataNaprawy: '2025-01-15'
  }
});

// Get specific report
const report = await ReportsService.getRaport({ id: newReport.id! });
```

### Working with Issues

```typescript
import { IssuesService } from '@drimain/api-client';

// List issues with filtering
const issues = await IssuesService.listZgloszenia({
  status: 'OPEN',
  typ: 'awaria',
  q: 'search-term'
});

// Create new issue
const newIssue = await IssuesService.createZgloszenie({
  requestBody: {
    typ: 'awaria',
    imie: 'Jan',
    nazwisko: 'Kowalski',
    opis: 'Opis problemu'
  }
});
```

### Working with Parts

```typescript
import { PartsService } from '@drimain/api-client';

// List parts with filtering
const parts = await PartsService.listParts({
  kat: 'silnik',
  belowMin: true
});

// Adjust part quantity (relative change)
const updatedPart = await PartsService.adjustPartQuantity({
  id: 1,
  requestBody: {
    delta: -5 // Remove 5 parts
  }
});

// Set absolute quantity
const updatedPart2 = await PartsService.adjustPartQuantity({
  id: 2,
  requestBody: {
    value: 25 // Set to exactly 25 parts
  }
});
```

### Error Handling

```typescript
import { ApiError } from '@drimain/api-client';

try {
  const report = await ReportsService.getRaport({ id: 999 });
} catch (error) {
  if (error instanceof ApiError) {
    console.error('API Error:', error.status, error.message);
  } else {
    console.error('Unexpected error:', error);
  }
}
```

### Token Management

The client supports automatic token refresh. You can set up token interceptors:

```typescript
import { OpenAPI } from '@drimain/api-client';

// Set token globally
OpenAPI.TOKEN = async () => {
  // Return current token or refresh it
  const token = await getStoredToken();
  return token;
};

// Or set it directly
OpenAPI.TOKEN = 'your-jwt-token';
```

## API Reference

The client provides the following services:

- **HealthService**: Health check endpoint
- **AuthenticationService**: User authentication and user info
- **ReportsService**: Repair reports management (CRUD + pagination)
- **IssuesService**: Issues/problems management (CRUD)
- **PartsService**: Spare parts inventory management (CRUD + quantity adjustments)

All services provide fully typed request/response objects based on the OpenAPI specification.

## Development

This client is auto-generated from `docs/openapi.yaml`. To regenerate:

```bash
npm run gen:api
```

Do not modify generated files directly. Instead, update the OpenAPI specification and regenerate the client.