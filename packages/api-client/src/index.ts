/**
 * Drimain API Client
 *
 * This module provides a type-safe client for communicating with the Drimain backend API.
 * It re-exports generated client code and provides additional utilities.
 */

// Re-export generated API client types and services
// Note: This will be populated after running `pnpm generate:api`
// export * from '../generated';

// Temporary placeholder exports until API generation is run
export interface ApiClient {
  // This will be replaced by generated types
}

// API Configuration
export interface ApiConfig {
  baseUrl: string;
  timeout?: number;
  headers?: Record<string, string>;
  interceptors?: {
    request?: (config: any) => any;
    response?: (response: any) => any;
    error?: (error: any) => any;
  };
}

// Default configuration
export const defaultConfig: ApiConfig = {
  baseUrl: process.env.API_BASE_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
};

// Utility functions for API client setup
export const createApiClient = (config: Partial<ApiConfig> = {}): ApiConfig => {
  return {
    ...defaultConfig,
    ...config,
    headers: {
      ...defaultConfig.headers,
      ...config.headers,
    },
  };
};

// Authentication utilities
export const addAuthHeader = (token: string): Record<string, string> => ({
  Authorization: `Bearer ${token}`,
});

// Error handling utilities
export const isApiError = (error: any): boolean => {
  return error && typeof error === 'object' && 'error' in error;
};

export const getErrorMessage = (error: any): string => {
  if (isApiError(error)) {
    return error.error.message || 'Unknown API error';
  }
  if (error instanceof Error) {
    return error.message;
  }
  return 'Unknown error occurred';
};

// Version information
export const VERSION = '0.1.0';
export const PACKAGE_INFO = {
  name: '@drimain/api-client',
  version: VERSION,
  description: 'Type-safe API client for Drimain backend services',
} as const;

// Instructions for usage after generation
export const USAGE_INSTRUCTIONS = `
To use this API client:

1. Update openapi/openapi.yaml with your actual API specification
2. Run: pnpm generate:api
3. The generated client will be available as exports from this module

Example usage after generation:
import { AuthService, UsersService } from '@drimain/api-client';

const authService = new AuthService();
const loginResult = await authService.login({
  username: 'user@example.com',
  password: 'password123'
});
`;

export default {
  createApiClient,
  addAuthHeader,
  isApiError,
  getErrorMessage,
  VERSION,
  PACKAGE_INFO,
};
