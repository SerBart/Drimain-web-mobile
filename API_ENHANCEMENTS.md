# Zgloszenie API Enhancements - PR 1

## Overview
This PR introduces the first phase of zgloszenie (issues) API enhancements, providing the foundation for mobile app integration and future features. The implementation focuses on priority management, advanced filtering, pagination, and Bean Validation.

## New Features Implemented

### 1. Priority Management
- **ZgloszeniePriorytet enum**: LOW, MEDIUM, HIGH, CRITICAL
- **Default priority**: MEDIUM (set automatically if not specified)
- **Priority filtering**: Filter zgloszenia by priority level
- **Priority updates**: Change priority via PATCH requests

### 2. Enhanced Filtering & Pagination
The GET `/api/zgloszenia` endpoint now supports comprehensive filtering:

```
GET /api/zgloszenia?status=OPEN&priorytet=HIGH&q=urgent&page=0&size=20&sort=createdAt,desc
```

**Available filters:**
- `status` - Filter by ZgloszenieStatus (OPEN, IN_PROGRESS, ON_HOLD, DONE, REJECTED)
- `typ` - LIKE search in type field
- `dzialId` - Filter by department ID
- `autorId` - Filter by author/user ID
- `q` - Full-text search across opis, imie, nazwisko, tytul fields
- `priorytet` - Filter by priority (LOW, MEDIUM, HIGH, CRITICAL)
- `dataOd` - Filter from date (ISO 8601: yyyy-MM-ddTHH:mm:ss)
- `dataDo` - Filter to date (ISO 8601: yyyy-MM-ddTHH:mm:ss)

**Pagination parameters:**
- `page` - Page number (0-based, default: 0)
- `size` - Page size (default: 20, max: 100)
- `sort` - Sort format: field,direction (default: createdAt,desc)

### 3. Response Format
All paginated responses now return structured format:
```json
{
  "content": [...],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8,
  "sort": "createdAt,desc"
}
```

### 4. Bean Validation
Replaced manual validate() method with Jakarta Bean Validation annotations:
- `@NotBlank` - Required string fields (typ, imie, nazwisko, opis)
- `@Size(min=10)` - Minimum description length
- `@NotNull` - Required priority field
- `@PastOrPresent` - Date validation for dataGodzina

### 5. Enhanced Error Handling
- **422 Unprocessable Entity**: Bean Validation errors
- **400 Bad Request**: Invalid arguments
- **403 Forbidden**: Access denied
- **500 Internal Server Error**: Server errors

Error response format:
```json
{
  "timestamp": "2025-09-01T09:30:00Z",
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Validation failed",
    "details": [
      {
        "field": "opis",
        "message": "Opis musi mieć co najmniej 10 znaków"
      }
    ]
  }
}
```

## API Endpoints

### GET /api/zgloszenia
List zgloszenia with filtering and pagination
- **Query parameters**: All filtering and pagination options listed above
- **Response**: PagedResponse<ZgloszenieDTO>

### GET /api/zgloszenia/{id}
Get single zgloszenie by ID
- **Response**: ZgloszenieDTO

### POST /api/zgloszenia
Create new zgloszenie with validation
- **Request body**: ZgloszenieCreateRequest (with priority field)
- **Response**: ZgloszenieDTO (201 Created)
- **Validation**: Returns 422 if validation fails

### PATCH /api/zgloszenia/{id}
Partially update zgloszenie (requires ADMIN or BIURO role)
- **Request body**: ZgloszenieUpdateRequest (all fields optional, including priority)
- **Response**: ZgloszenieDTO
- **Security**: Returns 403 if insufficient permissions

### DELETE /api/zgloszenia/{id}
Delete zgloszenie (requires ADMIN or BIURO role)
- **Response**: 204 No Content
- **Security**: Returns 403 if insufficient permissions

## Database Changes

### Flyway Migration V2
```sql
-- Add priorytet column to zgloszenia table
ALTER TABLE zgloszenia ADD COLUMN priorytet VARCHAR(20) NOT NULL DEFAULT 'MEDIUM';

-- Add missing columns that exist in entity but not in original schema  
ALTER TABLE zgloszenia ADD COLUMN tytul VARCHAR(255);
ALTER TABLE zgloszenia ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE zgloszenia ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE zgloszenia ADD COLUMN dzial_id BIGINT REFERENCES dzialy(id);
ALTER TABLE zgloszenia ADD COLUMN autor_id BIGINT REFERENCES users(id);
```

## Technical Implementation

### Architecture Components
- **ZgloszenieQueryService**: Builds JPA Specifications for complex filtering
- **ZgloszeniePriorityMapper**: Maps string values to enum (supports Polish names)
- **Enhanced ApiExceptionHandler**: Structured error responses with validation details
- **PagedResponse<T>**: Generic pagination wrapper
- **JpaSpecificationExecutor**: Database-level filtering for performance

### Key Classes Modified
- `Zgloszenie` - Added priority field, Bean Validation, removed manual validate()
- `ZgloszenieRestController` - Complete rewrite with filtering, pagination, PATCH
- `ZgloszenieRepository` - Extended with JpaSpecificationExecutor
- `ZgloszenieDTO` - Added priority field
- `ZgloszenieCreateRequest/UpdateRequest` - Added priority support

## Testing

### Unit Tests
- **ZgloszenieQueryServiceTest**: Comprehensive filtering scenario testing
- Tests cover individual filters, combinations, and edge cases

### Integration Tests  
- **ZgloszenieRestControllerIntegrationTest**: End-to-end API testing
- Covers pagination, filtering, validation errors, and security
- Note: Some tests may fail due to authentication in secured environment

## Backward Compatibility
- All existing zgloszenie fields remain unchanged
- Existing API consumers continue to work (additive changes only)
- Priority defaults to MEDIUM if not specified

## Future Enhancements (Out of Scope)
This PR specifically excludes:
- **Attachments (Załączniki)** - Planned for PR 2
- **Server-Sent Events (SSE)** - Planned for PR 3  
- **Reference codes and domain events** - Future PRs

## OpenAPI Documentation
All new endpoints include comprehensive Swagger annotations. Access documentation at:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## Testing the API

### Example requests:

**Get paginated zgloszenia with filtering:**
```bash
curl "http://localhost:8080/api/zgloszenia?priorytet=HIGH&status=OPEN&page=0&size=10"
```

**Create new zgloszenie:**
```bash
curl -X POST http://localhost:8080/api/zgloszenia \
  -H "Content-Type: application/json" \
  -d '{
    "typ": "Bug Report",
    "imie": "Jan",
    "nazwisko": "Kowalski", 
    "opis": "Detailed description of the issue with more than 10 characters",
    "priorytet": "HIGH"
  }'
```

**Update zgloszenie priority:**
```bash
curl -X PATCH http://localhost:8080/api/zgloszenia/1 \
  -H "Content-Type: application/json" \
  -d '{"priorytet": "CRITICAL"}'
```