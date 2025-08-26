"# DriMain - Unified Web & Mobile API

DriMain is a comprehensive maintenance management system with department-based access control and role-based permissions.

## Features

### 🔐 Authentication & Authorization
- JWT-based authentication with department information
- Role-based access control (ROLE_ADMIN, ROLE_BIURO, ROLE_USER)
- Department-based data visibility and access restrictions

### 📋 Zgłoszenia Management
- Create, read, update, delete zgłoszenia (reports/tickets)
- Department-based filtering (users see only their department's zgłoszenia)
- Status management: NEW, ACCEPTED, REJECTED, CLOSED
- Full audit trail with created/updated timestamps

### 🔧 Raport Management
- Maintenance report system (restricted to ROLE_ADMIN)
- Machine and person assignment
- Part usage tracking
- Status workflow management

### 🌐 API & TypeScript Client
- OpenAPI 3.0.3 specification
- Auto-generated TypeScript client
- Axios-based HTTP client with authentication helpers

## Architecture

### Department-Based Access Control

The system implements department-based access control with the following rules:

- **ROLE_ADMIN**: Full access to all data across all departments
- **ROLE_BIURO**: Global visibility for zgłoszenia, no access to raporty
- **Regular Users**: Access restricted to their assigned department's data

### Data Model

#### User-Department Relationship
- Each user can be assigned to one department (`User.dzial`)
- Department information is included in JWT tokens
- Department assignment affects data visibility

#### Zgłoszenia (Reports/Tickets)
- Linked to both department (`dzial`) and author (`autor`)
- Access controlled by user's department membership
- Status workflow: NEW → ACCEPTED/REJECTED → CLOSED

## API Documentation

The API follows RESTful principles with OpenAPI 3.0.3 specification available at `/docs/openapi.yaml`.

### Authentication Endpoints
- `POST /api/auth/login` - Authenticate and receive JWT
- `GET /api/auth/me` - Get current user information

### Zgłoszenia Endpoints
- `GET /api/zgloszenia` - List zgłoszenia (filtered by department)
- `POST /api/zgloszenia` - Create new zgłoszenie
- `GET /api/zgloszenia/{id}` - Get zgłoszenie details
- `PUT /api/zgloszenia/{id}` - Update zgłoszenie
- `DELETE /api/zgloszenia/{id}` - Delete zgłoszenie

### Raport Endpoints (ROLE_ADMIN only)
- `GET /api/raporty` - List raporty with filtering
- `POST /api/raporty` - Create new raport
- `GET /api/raporty/{id}` - Get raport details
- `PUT /api/raporty/{id}` - Update raport
- `DELETE /api/raporty/{id}` - Delete raport

## TypeScript Client

### Installation & Setup

```bash
# Install dependencies
npm install

# Validate OpenAPI spec
npm run lint:api

# Generate TypeScript client
npm run gen:api
```

### Usage Example

```typescript
import { configureApiClient, AuthenticationService, ZgOszeniaService } from './packages/api-client/client-helper';

// Configure API client
configureApiClient('http://localhost:8080');

// Authenticate
const authResponse = await AuthenticationService.login({
  username: 'user',
  password: 'password'
});

// Set authentication token
setAuthToken(authResponse.token);

// Use API services
const zgloszenia = await ZgOszeniaService.list({
  status: 'NEW',
  page: 0,
  size: 25
});
```

## Development

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 16+ (for TypeScript client generation)

### Running the Application

```bash
# Build and run
./mvnw spring-boot:run

# Run tests
./mvnw test

# Generate TypeScript client
npm run build:api
```

### Database

The application uses H2 for development with automatic schema creation. Flyway migrations handle schema updates:

- `V1_initial_schema.sql` - Initial database schema
- `V2_add_user_dzial.sql` - Add department relationship to users
- `V3_update_zgloszenia.sql` - Update zgłoszenia table with new fields

### Configuration

Key application properties:

```yaml
# JWT Configuration
jwt:
  expiration:
    minutes: 60
  secret:
    base64: "your-base64-secret"

# Database
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/drimain
    username: drimain
    password: drimain
```

## Security Considerations

- JWT tokens include user roles and department information
- All API endpoints (except health and auth) require authentication
- Department-based access control prevents data leakage between departments
- ROLE_BIURO provides controlled administrative access without full admin privileges

## Future Enhancements

See `UNIFICATION_PLAN.md` for planned improvements including:
- Multi-department user assignments
- Enhanced notification system
- Mobile app integration
- Advanced reporting capabilities

## Contributing

1. Follow the existing code style and patterns
2. Add tests for new functionality
3. Update API documentation when adding/changing endpoints
4. Regenerate TypeScript client after API changes" 
