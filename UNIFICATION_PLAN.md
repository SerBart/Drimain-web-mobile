# UNIFICATION_PLAN.md

This document outlines the planned improvements and standardization efforts for the DriMain API.

## TODOs

### Authentication & Security
- [ ] **Refresh tokens**: Implement refresh token mechanism for seamless authentication renewal
- [ ] **Token rotation**: Add automatic token rotation for enhanced security
- [ ] **Role-based access control**: Implement fine-grained permissions based on user roles
- [ ] **API rate limiting**: Add rate limiting to prevent abuse

### Data Management
- [ ] **Pagination for zgloszenia**: Implement Spring Data pageable support for incidents endpoints
- [ ] **Photo handling**: Implement proper photo upload/download endpoints with file storage
- [ ] **File attachments**: Add support for multiple file attachments per report/incident
- [ ] **Data validation**: Strengthen input validation and provide detailed error messages

### API Standardization
- [ ] **Standardized error schema**: Ensure all endpoints use consistent ErrorResponse format
- [ ] **Response normalization**: Standardize all API responses with consistent structure
- [ ] **HTTP status codes**: Review and ensure proper HTTP status code usage across all endpoints
- [ ] **API versioning**: Implement API versioning strategy for backward compatibility

### Performance Optimization
- [ ] **ID-only Raport representation**: Create lightweight Raport DTOs for list operations to reduce payload
- [ ] **Caching**: Implement caching for frequently accessed reference data (machines, persons, parts)
- [ ] **Database optimization**: Review and optimize database queries and indexes
- [ ] **Lazy loading**: Implement lazy loading for related entities to improve performance

### Monitoring & Observability
- [ ] **Health check enhancements**: Expand health checks to include database connectivity and external services
- [ ] **Metrics collection**: Add application metrics for monitoring and alerting
- [ ] **Logging improvements**: Standardize logging format and add structured logging
- [ ] **Request tracing**: Implement distributed tracing for better debugging

### Documentation & Developer Experience
- [ ] **API examples**: Add comprehensive examples to OpenAPI specification
- [ ] **SDK improvements**: Enhance generated client with better error handling and retry mechanisms
- [ ] **Integration tests**: Create comprehensive integration test suite
- [ ] **Postman collection**: Generate Postman collection from OpenAPI spec

### Business Logic
- [ ] **Workflow automation**: Implement status transition rules and validation
- [ ] **Notification system**: Add notification system for status changes and alerts
- [ ] **Audit trail**: Implement audit logging for all data changes
- [ ] **Reporting features**: Add advanced reporting and analytics capabilities

### Technical Debt
- [ ] **Code organization**: Refactor controllers to use service layer pattern consistently
- [ ] **Exception handling**: Implement global exception handling with proper HTTP status codes
- [ ] **DTO mapping**: Implement automated DTO mapping with MapStruct
- [ ] **Test coverage**: Increase test coverage to at least 80%

## Implementation Priority

1. **High Priority**: Standardized error schema, pagination for zgloszenia, photo handling
2. **Medium Priority**: Refresh tokens, ID-only representations, caching
3. **Low Priority**: Advanced monitoring, workflow automation, technical debt reduction

## Notes

- All changes should maintain backward compatibility where possible
- New features should include comprehensive tests
- API changes require documentation updates
- Performance improvements should be measured and validated