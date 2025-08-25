"# driMain

DriMain is a maintenance management system for industrial equipment.

## API

The application provides a comprehensive REST API for managing maintenance reports, incidents, and parts inventory.

### API Documentation

The API is documented using OpenAPI 3.0 specification. You can find the specification in `docs/openapi.yaml`.

### API Client Generation

To generate a TypeScript client for the API:

1. Install dependencies:
   ```bash
   npm install
   ```

2. Generate the API client:
   ```bash
   npm run gen:api
   ```

This will generate a TypeScript client in the `api-client` directory with full type safety and auto-completion.

### Available Endpoints

- **Authentication** (`/api/auth/`)
  - `POST /api/auth/login` - User authentication
  - `GET /api/auth/me` - Get current user info

- **Reports** (`/api/raporty/`)
  - `GET /api/raporty` - List reports (paginated)
  - `POST /api/raporty` - Create new report
  - `GET /api/raporty/{id}` - Get report by ID
  - `PUT /api/raporty/{id}` - Update report
  - `DELETE /api/raporty/{id}` - Delete report

- **Incidents** (`/api/zgloszenia/`)
  - `GET /api/zgloszenia` - List incidents
  - `POST /api/zgloszenia` - Create new incident
  - `GET /api/zgloszenia/{id}` - Get incident by ID
  - `PUT /api/zgloszenia/{id}` - Update incident
  - `DELETE /api/zgloszenia/{id}` - Delete incident

- **Parts** (`/api/czesci/`)
  - `GET /api/czesci` - List parts
  - `POST /api/czesci` - Create new part
  - `GET /api/czesci/{id}` - Get part by ID
  - `PUT /api/czesci/{id}` - Update part
  - `PATCH /api/czesci/{id}/ilosc` - Update part quantity
  - `DELETE /api/czesci/{id}` - Delete part

- **Health Check**
  - `GET /health` - Application health status (unauthenticated)

### Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

The `/health` endpoint and `/api/auth/**` endpoints are publicly accessible." 
