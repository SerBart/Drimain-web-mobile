# DriMain API Unification Plan

This document outlines future improvements and standardizations for the DriMain API to enhance consistency, security, and functionality.

## Authentication & Security Enhancements

### 1. Refresh Token Implementation
- **Current**: Only access tokens (JWT) with fixed expiration
- **Target**: Implement refresh token mechanism
- **Benefits**: Enhanced security, better user experience
- **Tasks**:
  - Add `RefreshTokenEntity` and repository
  - Modify `AuthResponse` to include `refreshToken`
  - Add `/api/auth/refresh` endpoint
  - Implement token rotation strategy
  - Add refresh token cleanup job

### 2. Enhanced Token Management
- **Current**: Basic JWT with user roles
- **Target**: More sophisticated token structure
- **Improvements**:
  - Add token versioning
  - Include session ID in tokens
  - Add token revocation mechanism
  - Implement device tracking

## Error Handling Standardization

### 3. Standardized Error Response Expansion
- **Current**: Basic `ErrorResponse` structure
- **Target**: Rich error information with internationalization
- **Enhancements**:
  - Add error codes (`E_PART_NOT_FOUND`, `E_INSUFFICIENT_QUANTITY`)
  - Include field-level validation errors
  - Add localization support (`message_key`, `locale`)
  - Implement error severity levels
  - Add help URLs for error resolution

```typescript
interface EnhancedErrorResponse {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    messageKey?: string;
    errorCode?: string;
    severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
    path: string;
    traceId?: string;
    fieldErrors?: FieldError[];
    helpUrl?: string;
}
```

## Pagination & Filtering

### 4. Unified Pagination for All Collections
- **Current**: Inconsistent pagination (some endpoints don't support it)
- **Target**: Standard pagination for all collection endpoints
- **Scope**:
  - Add pagination to `/api/zgloszenia`
  - Standardize pagination parameters across all endpoints
  - Implement cursor-based pagination for large datasets
  - Add total count optimization options

### 5. Advanced Filtering & Search
- **Current**: Basic query parameters
- **Target**: Sophisticated filtering system
- **Features**:
  - Date range filtering (`created_after`, `created_before`)
  - Multi-field search with operators (`name:contains:engine AND status:eq:ACTIVE`)
  - Saved filter presets
  - Full-text search integration

## File Management

### 6. File Upload & Management System
- **Current**: No file upload support
- **Target**: Comprehensive file handling
- **Features**:
  - Photo upload for zgłoszenia
  - Document attachments for raporty
  - Profile pictures for users
  - File validation and virus scanning
  - CDN integration for file serving
  - Automatic image optimization

```typescript
interface FileUploadResponse {
    fileId: string;
    filename: string;
    size: number;
    contentType: string;
    url: string;
    thumbnailUrl?: string;
}
```

## Real-time Features

### 7. WebSocket/SSE Events Integration
- **Current**: REST-only communication
- **Target**: Real-time updates for dynamic data
- **Events**:
  - `RaportChangedEvent` → WebSocket broadcast
  - `PartQuantityChangedEvent` → Inventory updates
  - `ZgloszenieStatusChangedEvent` → Status notifications
  - `UserActivityEvent` → Activity feeds

### 8. Notification System
- **Target**: Multi-channel notification delivery
- **Channels**:
  - In-app notifications
  - Email notifications
  - SMS for critical alerts
  - Push notifications for mobile
- **Events**:
  - Parts below minimum quantity
  - Overdue maintenance reports
  - New issue assignments

## Performance Optimizations

### 9. ID-only Embed Optimization
- **Current**: Full object embedding in responses
- **Target**: Configurable embedding levels
- **Benefits**: Reduced payload size, faster responses
- **Implementation**:
  - Add `embed` query parameter (`?embed=maszyna,osoba`)
  - Default to ID-only references
  - Support deep embedding (`?embed=maszyna.dzial`)

```typescript
// Before
interface Raport {
    maszyna: SimpleMaszynaDTO; // Full object always
}

// After
interface Raport {
    maszynaId: number;
    maszyna?: SimpleMaszynaDTO; // Only when requested
}
```

### 10. Caching Strategy
- **Target**: Multi-layer caching implementation
- **Levels**:
  - Database query caching (Redis)
  - HTTP response caching
  - CDN caching for static content
  - Client-side caching guidelines

## API Documentation

### 11. Enhanced OpenAPI Documentation
- **Current**: Basic OpenAPI 3.0.3 spec
- **Enhancements**:
  - Add request/response examples for all scenarios
  - Include error response examples
  - Add workflow documentation
  - Interactive API testing environment
  - SDK generation automation

### 12. API Versioning Strategy
- **Target**: Backward-compatible API evolution
- **Approach**:
  - URL versioning (`/api/v2/raporty`)
  - Header versioning support
  - Deprecation timeline communication
  - Migration guides

## Security Enhancements

### 13. Advanced Authorization
- **Current**: Role-based access control
- **Target**: Fine-grained permissions
- **Features**:
  - Resource-level permissions
  - Dynamic permission evaluation
  - Permission inheritance
  - Audit logging for all access

### 14. Rate Limiting & Throttling
- **Target**: Protect API from abuse
- **Features**:
  - Per-user rate limits
  - Endpoint-specific limits
  - Burst handling
  - Rate limit headers
  - Graceful degradation

## Monitoring & Analytics

### 15. API Observability
- **Target**: Comprehensive monitoring
- **Metrics**:
  - Request/response times
  - Error rates by endpoint
  - User activity patterns
  - System resource usage

### 16. Business Intelligence Integration
- **Target**: Data-driven insights
- **Features**:
  - Maintenance trend analysis
  - Parts usage patterns
  - Efficiency metrics
  - Predictive maintenance alerts

## Implementation Priority

1. **High Priority**: Refresh tokens, error standardization, pagination for zgloszenia
2. **Medium Priority**: File uploads, WebSocket events, caching
3. **Low Priority**: Advanced filtering, business intelligence, full observability

## Migration Strategy

Each enhancement will be implemented with backward compatibility in mind:
1. Add new features alongside existing ones
2. Provide migration period (minimum 3 months)
3. Communicate deprecation timeline
4. Remove deprecated features only after user migration

## Success Metrics

- API response time < 200ms for 95% of requests
- 99.9% API availability
- Zero breaking changes in minor versions
- Complete test coverage for all endpoints
- Developer satisfaction score > 8/10