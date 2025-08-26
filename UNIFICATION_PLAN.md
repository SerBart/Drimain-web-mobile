# UNIFICATION_PLAN.md

## System Architecture Evolution Plan

### Multi-Department Strategy

The system has been enhanced with department-based scoping for zgłoszenia management:

#### Current Implementation (Phase 1)
- Added department foreign key to User entity
- Implemented department-based access control for zgłoszenia
- Created ROLE_BIURO for office-level cross-department access
- Separated access rules: ADMIN/BIURO see all, regular users see only their department

#### Future Considerations

##### Multi-Department Expansion (Phase 2)
- Consider hierarchical department structures if needed
- Implement department-based reporting and analytics
- Add department-specific workflows and approval processes
- Enhance audit trails with department context

##### BIURO Role Strategy
The ROLE_BIURO provides a middle-ground access level:
- **Current Scope**: Full zgłoszenia access across departments
- **Restrictions**: No access to raporty (admin-only)
- **Future Expansion**: Could be extended with specific office management permissions
- **Alternative Approaches**: Could implement per-department biuro roles if needed

##### Access Control Evolution
- Current: Role-based + department-based for zgłoszenia
- Future: Consider resource-based permissions for fine-grained control
- Future: Implement approval workflows with department boundaries
- Future: Add delegation mechanisms for temporary cross-department access

#### Technical Debt and Improvements

##### Database Schema
- ✅ Added proper foreign keys and constraints
- ✅ Created migration scripts for schema updates
- 🔄 Consider adding department hierarchy support
- 🔄 Add indices for performance on department-based queries

##### Security Architecture
- ✅ JWT claims include department information
- ✅ Service-layer access control implementation
- ✅ Proper 403 responses for unauthorized access
- 🔄 Consider implementing audit logging for security events
- 🔄 Add rate limiting for API endpoints

##### Testing Strategy
- ✅ Comprehensive access control tests implemented
- ✅ Department-based filtering tests
- 🔄 Add integration tests for cross-department scenarios
- 🔄 Implement performance tests for large datasets with many departments

#### Scalability Considerations

##### Performance
- Department-based queries are indexed appropriately
- Pagination implemented for large result sets
- Consider caching for frequently accessed department data

##### Maintenance
- Clear separation of concerns between access control and business logic
- Service layer abstracts data access patterns
- Migration scripts ensure database schema evolution

### Implementation Status

- [x] Department-based data model
- [x] Access control service layer
- [x] REST API with proper error handling
- [x] JWT authentication with department claims
- [x] Comprehensive test coverage
- [x] Database migration scripts
- [x] Documentation updates

### Next Steps

1. Monitor production usage patterns for department-based access
2. Gather feedback on BIURO role permissions scope
3. Consider implementing department administration interface
4. Evaluate need for department hierarchy features
5. Plan for advanced reporting capabilities across departments