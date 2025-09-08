# Integration Test Suite - Testy Integracyjne

This directory contains comprehensive integration tests for the DriMain web application, organized by checkpoints that validate different aspects of the system.

## Test Checkpoints

### I-INIT: Application Initialization
**File**: `I_InitializationIntegrationTest.java`  
**Purpose**: Validates that the application initializes correctly with all required components.

**Tests**:
- I-INIT-001: Role initialization (ROLE_ADMIN, ROLE_USER, ROLE_MAGAZYN, ROLE_BIURO)
- I-INIT-002: Default admin user creation with correct roles
- I-INIT-003: Default regular user creation with correct roles  
- I-INIT-004: Unique role name constraints
- I-INIT-005: Password encryption validation

### I-RBAC: Role-Based Access Control
**File**: `I_RBACIntegrationTest.java`  
**Purpose**: Validates that role-based access control works correctly across API endpoints.

**Tests**:
- I-RBAC-001: Anonymous access to GET endpoints
- I-RBAC-002: Authentication required for modify operations
- I-RBAC-003: ADMIN full access permissions
- I-RBAC-004: BIURO edit/delete permissions 
- I-RBAC-005: USER read-only restrictions
- I-RBAC-006: MAGAZYN role restrictions
- I-RBAC-007: JWT token validation
- I-RBAC-008: Universal read access for authenticated users

### I-FALLBACK: Security Fallback & Error Handling  
**File**: `I_FallbackIntegrationTest.java`  
**Purpose**: Validates error handling, security edge cases, and fallback scenarios.

**Tests**:
- I-FALLBACK-001: Missing Authorization header handling
- I-FALLBACK-002: Malformed Authorization header handling
- I-FALLBACK-003: Expired JWT token handling
- I-FALLBACK-004: Invalid JWT token handling
- I-FALLBACK-005: Non-existent resource requests
- I-FALLBACK-006: Invalid request data handling
- I-FALLBACK-007: Insufficient permissions handling
- I-FALLBACK-008: SQL injection attempt protection
- I-FALLBACK-009: XSS attempt handling
- I-FALLBACK-010: Concurrent access scenarios

## Test Configuration

### Test Profile
Tests run with the `test` profile which provides:
- Isolated H2 in-memory database
- Reduced logging for faster execution
- Test-specific JWT configuration
- Disabled flyway migrations

### Test Data
Each test checkpoint creates its own test data:
- Default users (admin/admin123, user/user123)
- Test roles and permissions
- Sample zgloszenie records for testing

## Running Tests

### Individual Checkpoint Tests
```bash
# Run I-INIT checkpoint
./mvnw test -Dtest=I_InitializationIntegrationTest

# Run I-RBAC checkpoint  
./mvnw test -Dtest=I_RBACIntegrationTest

# Run I-FALLBACK checkpoint
./mvnw test -Dtest=I_FallbackIntegrationTest
```

### All Integration Tests
```bash
# Run all integration tests
./mvnw test -Dtest="**/*IntegrationTest"
```

### Full Test Suite
```bash
# Run all tests including unit tests
./mvnw test
```

## Integration with CI/CD

These tests validate:
1. **Security Compliance**: RBAC implementation follows security best practices
2. **API Consistency**: All endpoints respect authentication/authorization rules
3. **Error Handling**: Graceful handling of edge cases and security threats
4. **Data Integrity**: Proper initialization and data handling

## Test Results Validation

Each checkpoint must pass completely before the application can be considered ready for deployment. The tests validate:

- ✅ **I-INIT**: System initializes with proper roles and users
- ✅ **I-RBAC**: Role-based permissions work correctly 
- ✅ **I-FALLBACK**: Security edge cases handled properly

## Maintenance

When adding new roles or endpoints:
1. Update I-RBAC tests to include new permissions
2. Add fallback scenarios to I-FALLBACK tests
3. Update initialization tests if new roles are added
4. Document changes in this README

## Dependencies

Tests use:
- Spring Boot Test framework
- MockMvc for web layer testing
- H2 in-memory database
- JWT token generation for authentication testing
- AssertJ for fluent assertions