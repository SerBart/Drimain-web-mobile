# Drimain Web Application

Spring Boot web application with Thymeleaf templates, part of the Drimain monorepo.

## Quick Start

From the monorepo root:
```bash
pnpm dev:web
```

Or from this directory:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Development

### Prerequisites
- Java 17 or later
- Maven (via included wrapper)

### Scripts
- `pnpm dev` / `./mvnw spring-boot:run` - Start development server
- `pnpm build` / `./mvnw clean package` - Build the application
- `pnpm test` / `./mvnw test` - Run tests
- `pnpm clean` / `./mvnw clean` - Clean build artifacts

### Integration with Monorepo Packages

This application integrates with shared packages:

- **@drimain/core-domain**: Shared domain models and validation schemas
- **@drimain/design-tokens**: Shared design system tokens and CSS

The design tokens are automatically loaded in templates via:
```html
<link rel="stylesheet" th:href="@{/css/tokens.css}">
```

### Project Structure
```
apps/web/
├── src/main/
│   ├── java/drimer/drimain/    # Java source code
│   └── resources/
│       ├── static/             # Static assets (CSS, JS)
│       │   └── css/tokens.css  # Design tokens (auto-copied)
│       └── templates/          # Thymeleaf templates
├── pom.xml                     # Maven configuration
└── package.json                # npm scripts and workspace deps
```

### Configuration Profiles
- `local` - Development with H2 database
- `dev` - Development environment
- `prod` - Production environment

See `src/main/resources/application*.properties` for configuration details.