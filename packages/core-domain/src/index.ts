// Re-export all types and utilities
export * from './types';
export * from './mappers';

// Version
export const VERSION = '0.1.0';

// Package info
export const PACKAGE_INFO = {
  name: '@drimain/core-domain',
  version: VERSION,
  description: 'Core domain models and business logic for Drimain',
} as const;
