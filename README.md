"# driMain

Aplikacja do zarządzania zgłoszeniami i raportami w firmie produkcyjnej.

## API Documentation

### Zgłoszenia API

Endpoints for managing zgłoszenia (reports/tickets) with department-based access control.

#### Access Rules

- **ROLE_ADMIN**: Full access to all zgłoszenia across all departments
- **ROLE_BIURO**: Full access to all zgłoszenia across all departments  
- **ROLE_USER**: Access only to zgłoszenia from their assigned department
- **ROLE_MAGAZYN**: Access only to zgłoszenia from their assigned department

#### Authentication

All API endpoints require JWT authentication via `Authorization: Bearer <token>` header.

Department information is included in JWT claims (`deptId`, `deptName`) when user has assigned department.

#### Endpoints

##### GET /api/zgloszenia
List zgłoszenia with department-based filtering and pagination.

**Query Parameters:**
- `status` (optional): Filter by status (NEW, ACCEPTED, REJECTED, CLOSED)
- `page`, `size`, `sort` (optional): Pagination parameters

**Response:** Paginated list of ZgloszenieDTO objects

##### GET /api/zgloszenia/{id}
Get single zgłoszenie by ID with access control.

**Returns:** 
- 200: ZgloszenieDTO object
- 403: If user cannot access zgłoszenie from another department
- 404: If zgłoszenie not found

##### POST /api/zgloszenia
Create new zgłoszenie.

**Request Body:**
```json
{
  "tytul": "string (required)",
  "opis": "string (required)", 
  "status": "string (optional, defaults to NEW)",
  "dzialId": "number (optional, only for ADMIN/BIURO)"
}
```

**Access Rules:**
- Regular users: Forced to their own department (dzialId parameter ignored)
- ADMIN/BIURO: Can specify dzialId or use their own department as default

**Returns:**
- 201: Created ZgloszenieDTO
- 400: Validation errors

##### PUT /api/zgloszenia/{id}
Update existing zgłoszenie.

**Request Body:**
```json
{
  "tytul": "string (optional)",
  "opis": "string (optional)",
  "status": "string (optional)"
}
```

**Returns:**
- 200: Updated ZgloszenieDTO
- 403: If user cannot access zgłoszenie from another department

##### DELETE /api/zgloszenia/{id}
Delete zgłoszenie.

**Returns:**
- 204: Success
- 403: If user cannot access zgłoszenie from another department

#### ZgloszenieDTO Structure

```json
{
  "id": "number",
  "tytul": "string",
  "opis": "string", 
  "status": "NEW|ACCEPTED|REJECTED|CLOSED",
  "dzialId": "number",
  "dzialNazwa": "string",
  "autorUsername": "string",
  "createdAt": "datetime",
  "updatedAt": "datetime"
}
```

### Raporty API

**Access:** Restricted to ROLE_ADMIN only. ROLE_BIURO users receive 403 responses.

All raport endpoints require ADMIN role:
- GET /api/raporty
- POST /api/raporty  
- GET /api/raporty/{id}
- PUT /api/raporty/{id}
- DELETE /api/raporty/{id}

### Authentication API

##### POST /api/auth/login
Login with username/password.

**Request:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**
```json
{
  "token": "JWT token",
  "expiresAt": "datetime",
  "roles": ["ROLE_USER", "ROLE_ADMIN", etc.],
  "departmentId": "number (optional)",
  "departmentName": "string (optional)"
}
```

## Roles

- **ROLE_ADMIN**: Full system access
- **ROLE_BIURO**: Office role with cross-department access to zgłoszenia but no raport access
- **ROLE_USER**: Regular user with department-restricted access  
- **ROLE_MAGAZYN**: Warehouse user with department-restricted access

## Development

### Database Migrations

The application uses Flyway for database migrations:
- V2_add_user_department.sql: Adds department foreign key to users table
- V3_update_zgloszenia_structure.sql: Updates zgłoszenia table structure for department-based scoping

### Running Tests

```bash
mvn test
```

Department access rules are tested in `ZgloszenieAccessTest`." 
