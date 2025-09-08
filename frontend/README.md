# DriMain API TypeScript Client

This package provides a TypeScript client for the DriMain API, automatically generated from the OpenAPI specification.

## Installation

```bash
npm install
```

## Usage

### Basic Usage

```typescript
import { ApiClient, ZgloszeniaService, AuthService } from './src';
import { setAuthToken } from './src/config/apiClientConfig';

// Initialize the client
const client = new ApiClient();

// Authenticate
const authResponse = await AuthService.login({
  username: 'your-username',
  password: 'your-password'
});

// Set the auth token for subsequent requests
setAuthToken(authResponse.token);

// Use the API
const zgloszenia = await ZgloszeniaService.listZgloszenia();
```

### Authentication

The client automatically handles JWT token authentication. After calling `setAuthToken()`, all subsequent API calls will include the authorization header.

```typescript
import { setAuthToken, getAuthToken } from './src/config/apiClientConfig';

// Set token (usually after login)
setAuthToken('your-jwt-token');

// Get current token
const token = getAuthToken();
```

## API Generation

The client code is automatically generated from the OpenAPI specification. 

### Regenerating the Client

```bash
npm run gen
```

**⚠️ Important:** Do not manually edit files in the `src/api/` directory as they will be overwritten when regenerating the client.

### Available Scripts

- `npm run clean` - Remove generated files and build output
- `npm run gen` - Generate TypeScript client from OpenAPI spec
- `npm run check:spec` - Validate OpenAPI specification without generating files
- `npm run build` - Compile TypeScript to JavaScript
- `npm run lint` - Run ESLint and fix issues

## Generated API Structure

The generated client exposes the following services:

- `AuthService` - Authentication endpoints (`login`, `me`)
- `ZgloszeniaService` - Zgloszenia management (`listZgloszenia`, `createZgloszenie`, `getZgloszenie`, `updateZgloszenie`, `deleteZgloszenie`)
- `RaportyService` - Reports management (`listRaporty`)
- `PartsService` - Parts management (`listParts`)

## Requirements

- Node.js 16+ 
- TypeScript 5.0+
- The DriMain API server running and accessible

## Notes

- All API methods return Promises
- The client uses Axios for HTTP requests
- TypeScript definitions are automatically generated for all API models
- The auth token is automatically included in requests after calling `setAuthToken()`