"# DriMain

DriMain is a comprehensive web and mobile application for machine maintenance and parts management system.

## Features

- **Authentication & Authorization**: JWT-based authentication with role-based access control
- **Maintenance Reports**: Create, update, and track maintenance reports (raporty)
- **Parts Inventory**: Manage parts inventory with quantity tracking and automatic alerts
- **Issue Tracking**: Report and track maintenance issues (zgłoszenia)
- **Real-time Updates**: WebSocket integration for real-time data synchronization

## API & Client

DriMain provides a comprehensive REST API documented with OpenAPI 3.0.3 specification and includes a generated TypeScript client for easy integration.

### API Documentation

The complete API specification is available in `docs/openapi.yaml`. The API includes:

- **Authentication endpoints**: `/api/auth/login`, `/api/auth/me`
- **Reports management**: `/api/raporty` (CRUD operations with pagination)
- **Parts management**: `/api/czesci` (inventory management with quantity adjustments)  
- **Issue tracking**: `/api/zgloszenia` (issue reporting and status tracking)
- **Health check**: `/health` (application status monitoring)

### TypeScript Client

A fully typed TypeScript client is available in `packages/api-client/` with comprehensive type safety and easy-to-use API wrappers.

#### Usage Example

```typescript
import { createApiClient, RaportStatus } from '@drimain/api-client';

// Create client
const client = createApiClient('http://localhost:8080');

// Login
const auth = await client.auth.login({ username: 'admin', password: 'password' });
client.setToken(auth.token);

// Get user info
const user = await client.auth.me();

// List reports
const reports = await client.raporty.list({ 
    status: RaportStatus.NOWY,
    page: 0,
    size: 20 
});
```

### Client Generation

To regenerate the TypeScript client after API changes:

```bash
npm install
npm run gen:api
```

The generation process:
1. Validates the OpenAPI specification
2. Generates TypeScript types and API clients
3. Updates the `packages/api-client` directory
4. Commits the changes to the repository

### API Validation

To validate the OpenAPI specification:

```bash
npm run lint:api
```

## Development

### Prerequisites

- Java 17+
- Maven 3.8+
- Node.js 18+ (for client generation)

### Building

```bash
# Build Java application
./mvnw clean compile

# Run tests
./mvnw test

# Build TypeScript client
cd packages/api-client
npm run build
```

### Running

```bash
# Start the application
./mvnw spring-boot:run

# Access the application
# Web UI: http://localhost:8080
# API: http://localhost:8080/api
# Swagger UI: http://localhost:8080/swagger-ui.html
```

## Architecture

- **Backend**: Spring Boot 3.2.5 with Spring Security
- **Database**: H2 (development), configurable for production
- **Authentication**: JWT with configurable expiration
- **API Documentation**: OpenAPI 3.0.3 with Swagger UI
- **Client**: TypeScript with Axios HTTP client
- **Real-time**: WebSocket support for live updates

## Future Improvements

See [UNIFICATION_PLAN.md](UNIFICATION_PLAN.md) for detailed roadmap including:
- Refresh token implementation
- Enhanced error handling
- File upload support
- Advanced filtering and pagination
- Real-time notifications
- Performance optimizations" 
