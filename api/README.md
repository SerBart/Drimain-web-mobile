# Drimain API Documentation

This directory contains the OpenAPI specification and TypeScript client for the Drimain maintenance management API.

## API Endpoints

### Zgłoszenia (Issues/Reports)

- **GET /api/zgloszenia** - List zgłoszenia with pagination and filtering
  - Query params: `page` (int, default 0), `size` (int, default 20), `status` (enum)
  - Returns: Paginated list with metadata

- **POST /api/zgloszenia** - Create new zgłoszenie  
  - Body: `ZgloszenieCreateRequest` (only `opis` required)
  - Returns: Created zgłoszenie with generated ID and timestamps

- **GET /api/zgloszenia/{id}** - Get single zgłoszenie by ID
  - Path param: `id` (UUID string)
  - Returns: Single zgłoszenie details

- **PATCH /api/zgloszenia/{id}** - Partial update of zgłoszenie
  - Path param: `id` (UUID string) 
  - Body: `ZgloszenieUpdateRequest` (all fields optional)
  - Returns: Updated zgłoszenie

- **DELETE /api/zgloszenia/{id}** - Delete zgłoszenie
  - Path param: `id` (UUID string)
  - Returns: 204 No Content on success

## Fallback Title Generation

When creating or updating zgłoszenia, the `tytul` (title) field has automatic fallback generation:

- **On CREATE**: If `tytul` is `null` or empty/whitespace, the system auto-generates a title
- **On UPDATE**: If `tytul` is provided but is empty/whitespace, fallback regeneration is triggered
- **Format**: Generated titles typically follow pattern: `"AWARIA: [Author Name]"` or similar contextual format
- **Example**: When creating without title, you might get `"AWARIA: Jan Kowalski"` based on the authenticated user

## Status Values

The API uses the following status enumeration:

- `NOWE` - New/Open issue
- `W_TRAKCIE` - In progress  
- `ZAMKNIETE` - Closed/Completed

## Error Format

All API errors return a consistent JSON structure:

### 400 Validation Error
```json
{
  "error": "VALIDATION",
  "message": "Validation failed",
  "details": [
    "Opis is required",
    "Status must be valid"
  ]
}
```

### 403 Forbidden
```json
{
  "error": "FORBIDDEN", 
  "message": "Access denied. Admin or Biuro role required."
}
```

### 404 Not Found
```json
{
  "error": "NOT_FOUND",
  "message": "Zgłoszenie not found"
}
```

### 500 Internal Error
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred"
}
```

## TypeScript Client Usage

### Installation

Install the client locally using a relative path:

```bash
npm install ./api/ts-client
```

Or if using pnpm workspaces, add to your `package.json`:

```json
{
  "dependencies": {
    "@drimain/api-client": "workspace:*"
  }
}
```

### Basic Usage

```typescript
import { DrimainApiClient, ZgloszenieCreateRequest } from '@drimain/api-client';

// Initialize client
const client = new DrimainApiClient('http://localhost:8080');

// Create zgłoszenie with auto-generated title
const newZgloszenie: ZgloszenieCreateRequest = {
  opis: 'Problem z systemem chłodzenia',
  status: 'NOWE'
};

const created = await client.createZgloszenie(newZgloszenie);
console.log('Created:', created.tytul); // e.g., "AWARIA: Jan Kowalski"

// List zgłoszenia with pagination
const page = await client.listZgloszenia({ page: 0, size: 10, status: 'NOWE' });
console.log('Found:', page.totalElements, 'total items');

// Get single zgłoszenie
const details = await client.getZgloszenie(created.id);

// Update status
const updated = await client.updateZgloszenie(created.id, {
  status: 'W_TRAKCIE'
});

// Delete zgłoszenie  
await client.deleteZgloszenie(created.id);
```

### Error Handling

```typescript
try {
  const zgłoszenie = await client.getZgloszenie('invalid-id');
} catch (error) {
  if (error.error === 'NOT_FOUND') {
    console.log('Zgłoszenie not found');
  } else if (error.error === 'FORBIDDEN') {
    console.log('Access denied:', error.message);
  }
}
```

### Custom Fetch Implementation

You can provide a custom fetch implementation (useful for adding authentication):

```typescript
const customFetch = (url: string, options: RequestInit) => {
  return fetch(url, {
    ...options,
    headers: {
      ...options.headers,
      'Authorization': `Bearer ${getJwtToken()}`
    }
  });
};

const client = new DrimainApiClient('http://localhost:8080', customFetch);
```

## Regenerating the Client

To regenerate the TypeScript client after API changes:

1. Update the OpenAPI specification in `openapi.yaml`
2. Navigate to `ts-client/` directory
3. Run the build process:

```bash
cd api/ts-client
npm run build
```

The client is hand-written to keep the footprint minimal, so manual updates are required when the API changes.

## Future Extensions

- Sorting parameters are not yet implemented but may be added in future versions
- File upload support for zgłoszenie attachments is planned
- Additional filtering options (by author, date range) may be added