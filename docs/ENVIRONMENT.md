# Environment Configuration

This document standardizes environment variable usage across all applications in the Drimain monorepo.

## Environment Files

### Structure

```
.env.example          # Template with all variables
.env.local           # Local development (git-ignored)
.env.development     # Development defaults
.env.staging         # Staging configuration
.env.production      # Production configuration
```

### Loading Priority

1. `.env.local` (highest priority, git-ignored)
2. `.env.development` / `.env.staging` / `.env.production`
3. `.env` (fallback)

## Shared Environment Variables

### Backend Integration

```bash
# API Configuration
API_BASE_URL=http://localhost:8080
API_TIMEOUT=10000
API_RETRY_ATTEMPTS=3

# Authentication
JWT_SECRET_KEY=your-secret-key-here
JWT_EXPIRATION=3600
REFRESH_TOKEN_EXPIRATION=86400

# WebSocket
WS_ENDPOINT=ws://localhost:8080/ws
WS_RECONNECT_INTERVAL=5000
```

### Database Configuration

```bash
# Database (Spring Boot)
SPRING_DATASOURCE_URL=jdbc:h2:mem:drimain
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Production Database Example
# SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/drimain
# SPRING_DATASOURCE_USERNAME=drimain_user
# SPRING_DATASOURCE_PASSWORD=secure_password
```

### Application Configuration

```bash
# App Metadata
APP_NAME=Drimain
APP_VERSION=0.1.0
APP_ENVIRONMENT=development

# Feature Flags
FEATURE_OFFLINE_MODE=true
FEATURE_PUSH_NOTIFICATIONS=false
FEATURE_ANALYTICS=false
```

### External Services

```bash
# File Upload
UPLOAD_MAX_SIZE=10MB
UPLOAD_ALLOWED_TYPES=image/jpeg,image/png,application/pdf

# Email Service (future)
SMTP_HOST=
SMTP_PORT=587
SMTP_USERNAME=
SMTP_PASSWORD=
SMTP_FROM_EMAIL=noreply@drimain.com
```

### Security

```bash
# CORS Configuration
CORS_ORIGINS=http://localhost:3000,http://localhost:19006
CORS_METHODS=GET,POST,PUT,DELETE,PATCH
CORS_HEADERS=Content-Type,Authorization

# Rate Limiting
RATE_LIMIT_WINDOW=900000  # 15 minutes
RATE_LIMIT_MAX_REQUESTS=100
```

## Application-Specific Variables

### Web Application (Next.js)

```bash
# Next.js Configuration
NEXTAUTH_URL=http://localhost:3000
NEXTAUTH_SECRET=your-nextauth-secret

# Build Configuration
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_WS_ENDPOINT=ws://localhost:8080/ws
NEXT_PUBLIC_APP_ENV=development

# Analytics (future)
NEXT_PUBLIC_GA_ID=
NEXT_PUBLIC_SENTRY_DSN=
```

### Mobile Application (Expo/React Native)

```bash
# Expo Configuration
EXPO_PUBLIC_API_BASE_URL=http://localhost:8080
EXPO_PUBLIC_WS_ENDPOINT=ws://localhost:8080/ws
EXPO_PUBLIC_APP_ENV=development

# Mobile-specific
EXPO_PUBLIC_ENABLE_OFFLINE=true
EXPO_PUBLIC_SYNC_INTERVAL=30000

# Push Notifications
EXPO_PUSH_TOKEN=
FCM_SERVER_KEY=
APNS_KEY_ID=
APNS_TEAM_ID=
```

### Backend (Spring Boot)

```bash
# Server Configuration
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=development

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_DRIMER_DRIMAIN=DEBUG
LOGGING_FILE_NAME=logs/drimain.log

# Security
SPRING_SECURITY_OAUTH2_ENABLED=false
JWT_HEADER_NAME=Authorization
JWT_TOKEN_PREFIX=Bearer

# WebSocket
SPRING_WEBSOCKET_STOMP_ENDPOINT=/ws
SPRING_WEBSOCKET_ALLOWED_ORIGINS=http://localhost:3000
```

## Environment Templates

### .env.example

```bash
# ==============================================
# Drimain Environment Configuration Template
# ==============================================
# Copy this file to .env.local and fill in your values

# Backend API
API_BASE_URL=http://localhost:8080
JWT_SECRET_KEY=change-this-in-production

# Database
SPRING_DATASOURCE_URL=jdbc:h2:mem:drimain
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=

# Web App (Next.js)
NEXTAUTH_URL=http://localhost:3000
NEXTAUTH_SECRET=change-this-secret

# Mobile App (Expo)
EXPO_PUBLIC_API_BASE_URL=http://localhost:8080

# Feature Flags
FEATURE_OFFLINE_MODE=true
FEATURE_PUSH_NOTIFICATIONS=false

# Optional: External Services
SMTP_HOST=
SMTP_USERNAME=
SMTP_PASSWORD=
```

### .env.development

```bash
# Development Environment
APP_ENVIRONMENT=development
API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
EXPO_PUBLIC_API_BASE_URL=http://localhost:8080

# Development Database
SPRING_DATASOURCE_URL=jdbc:h2:mem:drimain
SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop
SPRING_H2_CONSOLE_ENABLED=true

# Logging
LOGGING_LEVEL_ROOT=DEBUG
LOGGING_LEVEL_DRIMER_DRIMAIN=TRACE

# Features
FEATURE_OFFLINE_MODE=true
FEATURE_ANALYTICS=false
```

### .env.production

```bash
# Production Environment
APP_ENVIRONMENT=production
API_BASE_URL=https://api.drimain.com
NEXT_PUBLIC_API_BASE_URL=https://api.drimain.com
EXPO_PUBLIC_API_BASE_URL=https://api.drimain.com

# Production Database
SPRING_DATASOURCE_URL=${DATABASE_URL}
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# Security
JWT_SECRET_KEY=${JWT_SECRET}
NEXTAUTH_SECRET=${NEXTAUTH_SECRET}

# Logging
LOGGING_LEVEL_ROOT=WARN
LOGGING_LEVEL_DRIMER_DRIMAIN=INFO

# Features
FEATURE_OFFLINE_MODE=true
FEATURE_PUSH_NOTIFICATIONS=true
FEATURE_ANALYTICS=true
```

## Validation Rules

### Required Variables

- `API_BASE_URL`: Must be valid URL
- `JWT_SECRET_KEY`: Minimum 32 characters for production
- `APP_ENVIRONMENT`: Must be one of: development, staging, production

### Format Validation

- URLs must include protocol (http/https)
- Ports must be valid numbers (1-65535)
- Email addresses must be valid format
- File sizes must include units (MB, GB)

### Security Requirements

- Secrets must not be committed to git
- Production secrets must be properly encrypted
- API keys must follow minimum length requirements
- Database passwords must meet complexity requirements

## Environment Loading

### Web Application (Next.js)

```typescript
// lib/env.ts
import { z } from 'zod';

const envSchema = z.object({
  NEXT_PUBLIC_API_BASE_URL: z.string().url(),
  NEXT_PUBLIC_APP_ENV: z.enum(['development', 'staging', 'production']),
  NEXTAUTH_SECRET: z.string().min(32),
});

export const env = envSchema.parse(process.env);
```

### Mobile Application (Expo)

```typescript
// config/env.ts
import { z } from 'zod';

const envSchema = z.object({
  EXPO_PUBLIC_API_BASE_URL: z.string().url(),
  EXPO_PUBLIC_APP_ENV: z.enum(['development', 'staging', 'production']),
});

export const env = envSchema.parse(process.env);
```

### Shared Package Usage

```typescript
// packages/core-domain/src/config.ts
export interface AppConfig {
  apiBaseUrl: string;
  environment: 'development' | 'staging' | 'production';
  features: {
    offlineMode: boolean;
    pushNotifications: boolean;
    analytics: boolean;
  };
}

export const createConfig = (env: Record<string, string>): AppConfig => ({
  apiBaseUrl: env.API_BASE_URL || 'http://localhost:8080',
  environment: (env.APP_ENVIRONMENT as any) || 'development',
  features: {
    offlineMode: env.FEATURE_OFFLINE_MODE === 'true',
    pushNotifications: env.FEATURE_PUSH_NOTIFICATIONS === 'true',
    analytics: env.FEATURE_ANALYTICS === 'true',
  },
});
```

## Deployment Considerations

### Docker

- Use multi-stage builds with environment-specific stages
- Pass environment variables through Docker Compose or Kubernetes
- Never bake secrets into Docker images

### CI/CD

- Store secrets in CI/CD platform secret management
- Use environment-specific deployment pipelines
- Validate environment variables before deployment

### Monitoring

- Monitor configuration drift between environments
- Alert on missing or invalid environment variables
- Track configuration changes in deployment logs

---

_This configuration will be expanded as new services and features are added to the platform._
