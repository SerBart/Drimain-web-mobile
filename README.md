"# DriMain - Web Mobile Application

DriMain is a comprehensive web application for managing repair reports, issues, and spare parts inventory. The application provides both web UI and REST API endpoints for managing industrial maintenance workflows.

## Features

- **Repair Reports Management**: Create, track, and manage repair reports with detailed information about machines, personnel, and parts used
- **Issues Tracking**: Submit and track maintenance issues with status updates and photo support
- **Parts Inventory**: Manage spare parts inventory with quantity tracking and low-stock alerts
- **User Authentication**: JWT-based authentication with role-based access control
- **REST API**: Complete OpenAPI 3.0 documented REST API with TypeScript client

## Technology Stack

- **Backend**: Spring Boot 3.2.5 with Java 17
- **Database**: H2 (development), configurable for production
- **Security**: Spring Security with JWT tokens
- **API Documentation**: OpenAPI 3.0 specification
- **Frontend Client**: Generated TypeScript client with fetch API
- **Build Tool**: Maven 3.x

## Quick Start

### Prerequisites

- Java 17 or higher
- Node.js 16.0+ (for API client generation)
- Maven 3.6+ (or use included wrapper)

### Running the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/SerBart/Drimain-web-mobile.git
   cd Drimain-web-mobile
   ```

2. **Build and run the backend**
   ```bash
   ./mvnw clean spring-boot:run
   ```

3. **Access the application**
   - Web UI: http://localhost:8080
   - Health Check: http://localhost:8080/health
   - API Documentation: http://localhost:8080/swagger-ui.html

### API Client Setup

The project includes a generated TypeScript client for frontend development:

1. **Install dependencies**
   ```bash
   npm install
   ```

2. **Generate/update API client** (when OpenAPI spec changes)
   ```bash
   npm run gen:api
   ```

3. **Use the client in your frontend**
   ```typescript
   import { DriMainApiClient, OpenAPI } from './packages/api-client';
   
   // Configure API base URL and authentication
   OpenAPI.BASE = 'http://localhost:8080';
   OpenAPI.TOKEN = 'your-jwt-token';
   
   // Use the client
   const client = new DriMainApiClient();
   ```

## API Documentation

The application provides a complete REST API with the following endpoints:

### Authentication
- `POST /api/auth/login` - User login (returns JWT token with expiration and roles)
- `GET /api/auth/me` - Get current user information

### Health Check
- `GET /health` - Service health status

### Repair Reports (Raporty)
- `GET /api/raporty` - List reports (paginated, with filtering and sorting)
- `POST /api/raporty` - Create new report
- `GET /api/raporty/{id}` - Get specific report
- `PUT /api/raporty/{id}` - Update report
- `DELETE /api/raporty/{id}` - Delete report

### Issues (Zgloszenia)
- `GET /api/zgloszenia` - List issues (with filtering)
- `POST /api/zgloszenia` - Create new issue
- `GET /api/zgloszenia/{id}` - Get specific issue
- `PUT /api/zgloszenia/{id}` - Update issue
- `DELETE /api/zgloszenia/{id}` - Delete issue

### Spare Parts (Czesci)
- `GET /api/czesci` - List parts (with filtering)
- `POST /api/czesci` - Create new part
- `GET /api/czesci/{id}` - Get specific part
- `PUT /api/czesci/{id}` - Update part
- `PATCH /api/czesci/{id}/ilosc` - Adjust part quantity (delta or absolute value)
- `DELETE /api/czesci/{id}` - Delete part

### API Specification

The complete API specification is available in `docs/openapi.yaml` and can be viewed via Swagger UI at `/swagger-ui.html`.

## TypeScript Client

The project includes a fully-typed TypeScript client generated from the OpenAPI specification:

### Features
- **Type Safety**: Complete TypeScript definitions for all endpoints and models
- **Modern API**: Uses fetch with Promise/async support
- **Cancellable Requests**: All requests return CancelablePromise
- **Authentication**: Built-in JWT Bearer token support
- **Tree Shakeable**: Import only the services you need

### Usage Examples

```typescript
import { AuthenticationService, ReportsService, PartsService } from './packages/api-client';

// Login and set token
const authResponse = await AuthenticationService.login({
  requestBody: { username: 'admin', password: 'password' }
});

// Get paginated reports
const reports = await ReportsService.listRaporty({
  status: 'NOWY',
  page: 0,
  size: 10
});

// Adjust part quantity
await PartsService.adjustPartQuantity({
  id: 1,
  requestBody: { delta: -5 } // Remove 5 parts
});
```

See `packages/api-client/README.md` for complete usage documentation.

## Authentication

The application uses JWT-based authentication with the following token structure (Variant B):

- **Single Access Token**: No refresh token (future enhancement)
- **Token Fields**: `token`, `expiresAt`, `roles`
- **Token Lifetime**: Configurable via `jwt.expiration.minutes` (default: 60 minutes)

### Login Flow

1. `POST /api/auth/login` with username/password
2. Receive JWT token with expiration and user roles
3. Include token in subsequent requests: `Authorization: Bearer <token>`
4. Use `GET /api/auth/me` to get current user info

## Development

### Project Structure

```
├── src/main/java/drimer/drimain/
│   ├── api/dto/              # Data Transfer Objects
│   ├── controller/           # REST Controllers
│   ├── model/               # JPA Entities
│   ├── repository/          # Data Repositories
│   ├── security/            # Security & JWT Configuration
│   └── service/            # Business Logic Services
├── docs/
│   └── openapi.yaml        # API Specification
├── packages/api-client/    # Generated TypeScript Client
└── package.json           # Node.js dependencies for client generation
```

### Building

```bash
# Backend only
./mvnw clean compile

# Run tests
./mvnw test

# Package application
./mvnw clean package

# Generate API client
npm run gen:api

# Validate OpenAPI spec
npm run gen:api:check
```

### Configuration

Application configuration is in `src/main/resources/application.properties`:

```properties
# JWT Configuration
jwt.expiration.minutes=60
jwt.secret.base64=<base64-encoded-secret>
# or
jwt.secret.plain=<plain-text-secret>

# Database (H2 default)
spring.datasource.url=jdbc:h2:mem:drimaindb
spring.jpa.hibernate.ddl-auto=create-drop
```

### Adding New Endpoints

1. Create/update DTOs in `api/dto/`
2. Add controller methods
3. Update `docs/openapi.yaml` specification
4. Regenerate TypeScript client: `npm run gen:api`

## Future Enhancements

The following features are planned for future releases:

- **Refresh Tokens**: Implement token variant A with refresh token support
- **Photo Handling**: Complete photo upload/storage for issues
- **Issue Pagination**: Add pagination support for issues endpoint  
- **Advanced Filtering**: Extended filtering options for reports
- **Real-time Updates**: WebSocket support for live data updates
- **Mobile App**: Native mobile application
- **Role-based UI**: Frontend role-based access control

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes and update OpenAPI spec if needed
4. Regenerate TypeScript client
5. Add tests for new functionality
6. Submit a pull request

## License

This project is licensed under the MIT License." 
