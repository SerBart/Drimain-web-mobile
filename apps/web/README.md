# Drimain Web Application

## Overview

This is the web application component of the Drimain unified platform. It uses the shared `@drimain/api-client` package for API communication.

## Current Status

**Bootstrap phase** - Basic API service integration completed as part of PR #2b.

## Structure

```
apps/web/
├── src/
│   └── services/
│       └── api.ts          # API client wrapper and services
├── package.json            # Web app dependencies
└── README.md              # This file
```

## Development

### Prerequisites

- Node.js 20+
- pnpm 8+
- Built `@drimain/api-client` package

### Setup

```bash
# From repository root
pnpm install
pnpm build  # Builds api-client package

# From apps/web
cd apps/web
# TODO: Add framework-specific setup commands
```

## API Integration

The web application integrates with the backend API through the `@drimain/api-client` package:

### Services Available

- **authService**: Login, register, logout, token refresh
- **userService**: Get and update user information  
- **profileService**: Get and update user profiles
- **healthService**: API health checking

### Example Usage

```typescript
import { authService, userService } from './src/services/api';

// Login
const tokens = await authService.login('user@example.com', 'password');

// Get current user
const user = await userService.getMe();

// Update user
const updatedUser = await userService.updateMe('New Name');
```

## TODO: Future Implementation

This is a minimal bootstrap. Future PRs will add:

### Framework Setup
- [ ] Choose and setup frontend framework (React, Vue, etc.)
- [ ] Setup build tools (Vite, Webpack, etc.)
- [ ] Configure routing
- [ ] Setup UI component library

### Authentication
- [ ] Login/register forms
- [ ] Token storage and management
- [ ] Protected route handling
- [ ] Auto-refresh token logic
- [ ] Logout handling

### User Interface
- [ ] User dashboard
- [ ] Profile management forms
- [ ] Settings pages
- [ ] Error and loading states
- [ ] Responsive design

### Advanced Features
- [ ] Real-time updates
- [ ] File upload handling
- [ ] Push notification support
- [ ] Offline functionality
- [ ] PWA features

### Development Tools
- [ ] TypeScript configuration
- [ ] Linting and formatting
- [ ] Testing setup (unit, integration, e2e)
- [ ] Development server
- [ ] Build and deployment scripts

## Integration Notes

- Uses shared `@drimain/api-client` for consistent API communication
- Follows workspace conventions for easy dependency management
- Designed for easy integration with any frontend framework
- Prepared for authentication token management
- Structured for scalable service layer architecture