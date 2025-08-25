# Unification Plan - Drimain Web + Mobile

## Project Overview

This project aims to unify the web and mobile applications for Drimain using a shared API and frontend monorepo structure.

## Architecture

```
drimain-web-mobile/
├── src/                    # Java Spring Boot backend
├── packages/
│   ├── api-client/         # Generated TypeScript API client
│   └── core-domain/        # Shared domain models (future)
├── apps/
│   ├── web/               # Web application (React/Vue/etc)
│   └── mobile/            # Mobile application (React Native/etc)
├── openapi.yaml           # API specification
└── tools/
    └── verify-model-sync.js  # Model sync validation tool
```

## Implementation Status

### STATUS

**OpenAPI baseline (auth + users + profiles + register) integrated in PR #2b** ✅

### Phase 1: API Foundation (PR #2b) ✅
- [x] OpenAPI specification with core endpoints
- [x] Authentication endpoints (login, register, refresh, logout)  
- [x] User management endpoints (GET/PATCH /users/me)
- [x] Profile endpoints (GET/PATCH /profiles/me, GET /profiles/{userId})
- [x] TypeScript client generation via openapi-generator
- [x] Barrel exports for easy client consumption  
- [x] Smoke tests for generated client
- [x] Model synchronization validation tool
- [x] Monorepo structure setup with pnpm workspaces

### Phase 2: Web Application Integration (PR #3)
- [ ] Mobile application migration to use shared API client
- [ ] Authentication flow implementation
- [ ] Profile management UI
- [ ] Error handling and loading states

### Phase 3: Mobile Application Integration (PR #4)
- [ ] React Native setup in apps/mobile
- [ ] Authentication flow for mobile
- [ ] Profile management screens
- [ ] Push notification integration

### Phase 4: Advanced Features
- [ ] Real-time features (WebSocket/SSE)
- [ ] File upload handling
- [ ] Advanced user management
- [ ] Notification system
- [ ] Admin panel features

## Technical Decisions

### API Client Generation
- **Tool**: OpenAPI Generator with typescript-axios template
- **Location**: `packages/api-client/src/generated/`
- **Exports**: Barrel exports in `packages/api-client/src/index.ts`
- **Testing**: Smoke tests to verify import functionality

### Model Synchronization
- **Tool**: Custom Node.js script `tools/verify-model-sync.js`
- **Validation**: Enum values and model key presence
- **Scope**: User, Profile, NotificationPrefs, UserRole, UserStatus

### Workspace Management
- **Tool**: pnpm workspaces
- **Structure**: packages/* and apps/* pattern
- **Scripts**: Centralized in root package.json

## Next Steps

1. **PR #3**: Implement basic web application integration
2. **PR #4**: Mobile application setup and API integration
3. **PR #5**: Enhanced error handling and user experience
4. **PR #6**: Real-time features and advanced functionality

## Deployment Strategy

### Backend (Java Spring Boot)
- Existing deployment process unchanged
- OpenAPI spec served at `/v3/api-docs` endpoint
- Environment-specific server URLs in openapi.yaml

### Frontend Applications
- Web: Standard SPA deployment (Netlify/Vercel/etc)
- Mobile: Standard app store deployment process
- Shared API client package for consistent behavior

## Developer Workflow

1. **API Changes**: Update openapi.yaml → regenerate clients → update applications
2. **Model Validation**: Run `pnpm verify-model-sync` before commits
3. **Testing**: Run `pnpm test` to validate all packages
4. **Building**: Run `pnpm build` for production builds

## Security Considerations

- JWT token-based authentication
- Refresh token rotation (when implemented)
- HTTPS enforcement in production
- CORS configuration for web clients
- Certificate pinning for mobile (future)