# Drimain Unification Plan

## Overview
This document tracks the progress of unifying the Drimain platform into a cohesive monorepo structure supporting both web and mobile applications with shared packages.

## Migration Phases

### Wave 1: Foundation & Web Migration ✅
**STATUS: Web application migrated to apps/web in PR #2**

- [x] Create monorepo structure (apps/, packages/)
- [x] Set up pnpm workspace configuration
- [x] Create shared packages:
  - [x] @drimain/core-domain - Domain models and validation schemas
  - [x] @drimain/design-tokens - Shared design system tokens
- [x] Migrate existing web application to apps/web/
- [x] Integrate design tokens CSS import
- [x] Add domain model integration demonstration
- [x] Update build and development scripts
- [x] Create comprehensive documentation

**Deliverables:**
- Monorepo workspace configuration
- Web application in apps/web/ with preserved functionality
- Shared design tokens package with CSS generation
- Shared domain models package with Zod schemas
- Updated documentation and development workflows

### Wave 2: Mobile Integration (Planned - PR #3)
- [ ] Migrate mobile application to apps/mobile/
- [ ] Integrate shared packages in mobile app
- [ ] Cross-platform testing setup
- [ ] Mobile-specific design token adaptations

### Wave 3: API Enhancement (Planned - PR #2b)
- [ ] Replace OpenAPI placeholder implementation
- [ ] Generate TypeScript client from OpenAPI spec
- [ ] Integrate generated client in web application
- [ ] Extend verify-model-sync tooling

## Architecture Decisions

### Package Management
- **Tool**: pnpm with workspaces
- **Rationale**: Efficient dependency management, fast installs, workspace support

### Shared Packages Structure
```
packages/
├── core-domain/       # Domain models, validation schemas
├── design-tokens/     # Design system tokens and CSS
└── (future packages)  # API clients, utilities, etc.
```

### Application Structure
```
apps/
├── web/              # Spring Boot web application
└── mobile/           # (Future) React Native mobile app
```

## Integration Strategy

### Design Tokens
- CSS custom properties generated from JavaScript tokens
- Automatic build process with `build-tokens.js`
- Integration via static CSS imports in web templates
- Future: React Native StyleSheet generation

### Domain Models
- Zod schemas for runtime validation
- TypeScript types generated from schemas
- Cross-platform validation logic
- Future: Code generation for other languages

## Development Workflow

### Getting Started
```bash
# Install all dependencies
pnpm install

# Start web development
pnpm dev:web

# Build all packages
pnpm build
```

### Adding New Shared Packages
1. Create package in `packages/[package-name]/`
2. Add to pnpm-workspace.yaml if needed
3. Configure TypeScript paths in tsconfig.base.json
4. Update dependent applications

## Status Summary

- ✅ **Foundation**: Monorepo structure established
- ✅ **Web Migration**: Complete with preserved functionality
- ✅ **Shared Packages**: Core domain and design tokens implemented
- ✅ **Documentation**: Comprehensive setup and usage guides
- ⏳ **Mobile Migration**: Planned for next phase
- ⏳ **API Enhancement**: Planned after mobile integration

## Next Steps

1. **PR #2b**: OpenAPI integration and TypeScript client generation
2. **PR #3**: Mobile application migration
3. **Wave 2+**: Advanced tooling and cross-platform optimizations

---
*Last updated: PR #2 - Web Application Migration*