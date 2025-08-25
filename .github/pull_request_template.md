# Pull Request Template

## Description

<!-- Provide a brief description of what this PR does -->

## Type of change

Please delete options that are not relevant:

- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactor (code change that neither fixes a bug nor adds a feature)
- [ ] Performance improvement
- [ ] Test coverage improvement
- [ ] Chore (tooling, dependencies, etc.)

## Scope

Please check all that apply:

- [ ] Backend (Java/Spring Boot)
- [ ] Frontend Web (React/Next.js)
- [ ] Mobile (React Native)
- [ ] Core Domain (shared types and logic)
- [ ] API Client (API integration)
- [ ] Design Tokens (design system)
- [ ] Documentation
- [ ] CI/CD
- [ ] Tooling/Configuration

## Checklist

Please check all items that apply to your PR:

### Code Quality

- [ ] My code follows the style guidelines of this project
- [ ] I have performed a self-review of my code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] My changes generate no new warnings
- [ ] I have run `pnpm lint` and fixed any issues
- [ ] I have run `pnpm typecheck` and fixed any type errors

### Testing

- [ ] I have added tests that prove my fix is effective or that my feature works
- [ ] New and existing unit tests pass locally with my changes
- [ ] I have tested the changes manually
- [ ] I have verified the changes work in different browsers (if web)
- [ ] I have tested on different devices/screen sizes (if UI changes)

### Backend Integration

- [ ] I have updated the OpenAPI specification (if backend changes)
- [ ] I have run `pnpm verify:models` and ensured model synchronization
- [ ] I have tested the API endpoints manually or with automated tests
- [ ] Database migrations are included (if applicable)

### Documentation

- [ ] I have updated relevant documentation
- [ ] I have updated the FEATURE-PARITY.md file (if applicable)
- [ ] I have added/updated code comments where necessary
- [ ] README or other docs have been updated (if applicable)

### Breaking Changes

- [ ] This PR includes breaking changes
- [ ] I have documented the breaking changes in the PR description
- [ ] I have updated the migration guide (if applicable)
- [ ] I have considered backward compatibility

## Related Issues

<!-- Link any related issues -->

Fixes #(issue)
Closes #(issue)
Related to #(issue)

## Screenshots

<!-- If your changes include visual updates, please provide screenshots -->

### Before:

<!-- Screenshot of the current state -->

### After:

<!-- Screenshot of the new state -->

## Testing Instructions

<!-- Describe how reviewers can test your changes -->

1. Checkout this branch
2. Install dependencies: `pnpm install`
3. Build packages: `pnpm build`
4. Run specific commands or navigate to specific URLs
5. Verify expected behavior

## Performance Impact

<!-- Describe any performance implications -->

- [ ] No performance impact
- [ ] Performance improvement
- [ ] Potential performance regression (please describe)
- [ ] Performance impact unknown/needs testing

## Security Considerations

<!-- Describe any security implications -->

- [ ] No security implications
- [ ] Security improvement
- [ ] Potential security risk (please describe and justify)
- [ ] Security review required

## Deployment Notes

<!-- Any special deployment considerations -->

- [ ] No special deployment requirements
- [ ] Database migration required
- [ ] Environment variable changes required
- [ ] Configuration changes required
- [ ] Third-party service updates required

## Additional Notes

<!-- Any additional information that reviewers should know -->

## Review Checklist for Maintainers

<!-- For maintainers - do not edit -->

- [ ] Code quality and style
- [ ] Test coverage adequate
- [ ] Documentation updated
- [ ] Breaking changes properly handled
- [ ] Performance impact acceptable
- [ ] Security implications reviewed
- [ ] Model synchronization verified (if applicable)
