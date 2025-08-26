# API Client Documentation

This document provides detailed information about using the generated TypeScript API client for DriMain.

## Overview

The TypeScript API client is auto-generated from the OpenAPI 3.0.3 specification using `openapi-typescript-codegen`. It provides type-safe access to all DriMain API endpoints with proper authentication handling.

## Client Generation

### Prerequisites
- Node.js 16+
- npm or yarn package manager

### Generate Client

```bash
# Install dependencies
npm install

# Validate OpenAPI specification
npm run lint:api

# Generate TypeScript client
npm run gen:api

# Or run both in sequence
npm run build:api
```

The generated client will be created in `packages/api-client/` directory.

## Client Structure

```
packages/api-client/
├── core/           # Core client functionality
│   ├── ApiError.ts
│   ├── OpenAPI.ts
│   └── request.ts
├── models/         # TypeScript interfaces for all API models
│   ├── AuthRequest.ts
│   ├── AuthResponse.ts
│   ├── UserInfo.ts
│   ├── Zgloszenie.ts
│   └── ...
├── services/       # API service classes
│   ├── AuthenticationService.ts
│   ├── ZgOszeniaService.ts
│   └── RaportyService.ts
├── index.ts        # Main exports
└── client-helper.ts # Configuration helpers
```

## Basic Usage

### 1. Configuration

```typescript
import { configureApiClient } from './packages/api-client/client-helper';

// Configure base URL and optional default token
configureApiClient('http://localhost:8080', 'optional-jwt-token');
```

### 2. Authentication

```typescript
import { AuthenticationService, setAuthToken } from './packages/api-client/client-helper';

// Login
try {
  const response = await AuthenticationService.login({
    username: 'your-username',
    password: 'your-password'
  });
  
  // Store token and configure client
  setAuthToken(response.token);
  
  console.log('Authenticated user:', response);
  console.log('Token expires at:', response.expiresAt);
  console.log('User roles:', response.roles);
  console.log('Department:', response.deptName);
} catch (error) {
  console.error('Authentication failed:', error);
}

// Get current user info
const userInfo = await AuthenticationService.me();
console.log('Current user:', userInfo);
```

### 3. Working with Zgłoszenia

```typescript
import { ZgOszeniaService, ZgloszenieStatus } from './packages/api-client/client-helper';

// List zgłoszenia with pagination and filtering
const zgloszenia = await ZgOszeniaService.list({
  status: ZgloszenieStatus.NEW,
  page: 0,
  size: 25,
  sort: 'createdAt,desc'
});

console.log('Total elements:', zgloszenia.totalElements);
console.log('Current page:', zgloszenia.page);
console.log('Zgłoszenia:', zgloszenia.content);

// Create new zgłoszenie
const newZgloszenie = await ZgOszeniaService.create({
  tytul: 'Network connectivity issue',
  opis: 'Unable to connect to company network from workstation in office 3A',
  status: ZgloszenieStatus.NEW,
  // dzialId is optional - will use user's department if not specified
});

// Get specific zgłoszenie
const zgloszenie = await ZgOszeniaService.get(newZgloszenie.id);

// Update zgłoszenie
const updated = await ZgOszeniaService.update(zgloszenie.id, {
  status: ZgloszenieStatus.ACCEPTED,
  opis: 'Issue confirmed. Scheduling maintenance for next week.'
});

// Delete zgłoszenie
await ZgOszeniaService.delete(zgloszenie.id);
```

### 4. Working with Raporty (Admin only)

```typescript
import { RaportyService, RaportStatus } from './packages/api-client/client-helper';

// List raporty with filtering
const raporty = await RaportyService.list({
  status: RaportStatus.W_TOKU,
  from: '2024-01-01',
  to: '2024-01-31',
  page: 0,
  size: 25
});

// Create new raport
const raport = await RaportyService.create({
  typNaprawy: 'Preventive maintenance',
  opis: 'Regular maintenance of conveyor belt system',
  dataNaprawy: '2024-01-15',
  czasOd: '08:00',
  czasDo: '12:00',
  maszynaId: 1,
  osobaId: 1,
  partUsages: [
    { partId: 1, ilosc: 2 },
    { partId: 2, ilosc: 1 }
  ]
});
```

## Error Handling

The client provides structured error handling:

```typescript
import { ApiError } from './packages/api-client';

try {
  const result = await ZgOszeniaService.list();
} catch (error) {
  if (error instanceof ApiError) {
    console.log('HTTP Status:', error.status);
    console.log('Error message:', error.message);
    console.log('Response body:', error.body);
    
    switch (error.status) {
      case 401:
        // Handle authentication error
        console.log('Authentication required');
        break;
      case 403:
        // Handle authorization error
        console.log('Access denied');
        break;
      case 404:
        // Handle not found
        console.log('Resource not found');
        break;
      default:
        console.log('Unexpected error:', error.message);
    }
  } else {
    console.error('Network or other error:', error);
  }
}
```

## Advanced Configuration

### Custom Headers

```typescript
import { OpenAPI } from './packages/api-client';

OpenAPI.HEADERS = {
  'Content-Type': 'application/json',
  'X-Custom-Header': 'custom-value',
  'Authorization': `Bearer ${token}`
};
```

### Request Interceptors

```typescript
import { OpenAPI } from './packages/api-client';

// Custom request interceptor
OpenAPI.ENCODE_PATH = (path: string) => {
  // Custom path encoding logic
  return encodeURI(path);
};
```

### Base URL Configuration

```typescript
import { OpenAPI } from './packages/api-client';

// Set base URL
OpenAPI.BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Set default credentials
OpenAPI.CREDENTIALS = 'include';
```

## Type Safety

The client provides full TypeScript support:

```typescript
import type { 
  Zgloszenie, 
  ZgloszenieCreateRequest, 
  ZgloszenieUpdateRequest,
  ZgloszeniePage,
  UserInfo,
  AuthResponse 
} from './packages/api-client';

// Type-safe function
function processZgloszenia(zgloszenia: ZgloszeniePage): void {
  zgloszenia.content.forEach((zgloszenie: Zgloszenie) => {
    console.log(`${zgloszenie.id}: ${zgloszenie.tytul}`);
    console.log(`Status: ${zgloszenie.status}`);
    console.log(`Department: ${zgloszenie.dzialNazwa}`);
    console.log(`Created: ${zgloszenie.createdAt}`);
  });
}
```

## Integration Examples

### React Hook

```typescript
import { useState, useEffect } from 'react';
import { ZgOszeniaService, type ZgloszeniePage } from './packages/api-client/client-helper';

function useZgloszenia(page: number = 0, size: number = 25) {
  const [data, setData] = useState<ZgloszeniePage | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchZgloszenia = async () => {
      setLoading(true);
      setError(null);
      
      try {
        const result = await ZgOszeniaService.list({ page, size });
        setData(result);
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Unknown error');
      } finally {
        setLoading(false);
      }
    };

    fetchZgloszenia();
  }, [page, size]);

  return { data, loading, error };
}
```

### Vue Composable

```typescript
import { ref, computed } from 'vue';
import { ZgOszeniaService, type ZgloszeniePage } from './packages/api-client/client-helper';

export function useZgloszenia() {
  const zgloszenia = ref<ZgloszeniePage | null>(null);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const fetchZgloszenia = async (page: number = 0, size: number = 25) => {
    loading.value = true;
    error.value = null;

    try {
      zgloszenia.value = await ZgOszeniaService.list({ page, size });
    } catch (err) {
      error.value = err instanceof Error ? err.message : 'Unknown error';
    } finally {
      loading.value = false;
    }
  };

  const totalPages = computed(() => zgloszenia.value?.totalPages || 0);
  const hasData = computed(() => !!zgloszenia.value?.content?.length);

  return {
    zgloszenia: readonly(zgloszenia),
    loading: readonly(loading),
    error: readonly(error),
    totalPages,
    hasData,
    fetchZgloszenia
  };
}
```

## Department-Based Access Patterns

Understanding department-based access control in the client:

```typescript
// Users with ROLE_ADMIN or ROLE_BIURO see all zgłoszenia
// Regular users see only their department's zgłoszenia
const allZgloszenia = await ZgOszeniaService.list();

// When creating zgłoszenia:
// - ADMIN/BIURO can specify dzialId
// - Regular users are forced to their own department
const zgloszenie = await ZgOszeniaService.create({
  tytul: 'Issue title',
  opis: 'Issue description',
  dzialId: 5 // Only works for ADMIN/BIURO users
});

// Access control is enforced server-side
// 403 Forbidden will be returned for unauthorized access
try {
  const raport = await RaportyService.list(); // Only ROLE_ADMIN
} catch (error) {
  if (error.status === 403) {
    console.log('Raporty access requires ROLE_ADMIN');
  }
}
```

## Troubleshooting

### Common Issues

1. **401 Unauthorized**
   - Token expired or invalid
   - Use `setAuthToken()` with fresh token

2. **403 Forbidden**  
   - Insufficient permissions
   - Check user roles and department assignments

3. **CORS Issues**
   - Configure server CORS settings
   - Ensure proper origin headers

4. **Type Errors**
   - Regenerate client after API changes
   - Check OpenAPI specification validity

### Regenerating Client

If the API changes, regenerate the client:

```bash
# Regenerate after API updates
npm run build:api

# Check for API specification errors
npm run lint:api
```

## Best Practices

1. **Token Management**
   - Store tokens securely
   - Handle token refresh
   - Clear tokens on logout

2. **Error Handling**
   - Implement consistent error handling
   - Show user-friendly error messages
   - Log errors for debugging

3. **Type Safety**
   - Use TypeScript interfaces
   - Validate response data
   - Handle null/undefined values

4. **Performance**
   - Implement pagination properly
   - Cache responses when appropriate
   - Use loading states in UI

5. **Security**
   - Never log sensitive data
   - Use HTTPS in production
   - Validate user inputs