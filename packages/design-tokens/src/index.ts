// Re-export generated tokens
export * from '../dist/tokens';

// Re-export tokens as default export for convenience
import tokens from '../dist/tokens';
export default tokens;

// Package metadata
export const VERSION = '0.1.0';
export const PACKAGE_INFO = {
  name: '@drimain/design-tokens',
  version: VERSION,
  description: 'Design system tokens for Drimain applications',
} as const;
