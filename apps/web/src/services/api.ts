/**
 * API Service for Drimain Web Application
 * 
 * This module provides a centralized API client instance and utility functions
 * for the web application. It wraps the generated @drimain/api-client.
 * 
 * TODO: This is a minimal bootstrap for PR #2b. Future enhancements needed:
 * - Add proper error handling
 * - Implement token management and refresh logic  
 * - Add request/response interceptors
 * - Set up proper base URL configuration
 * - Add loading states management
 * - Implement proper TypeScript types
 */

import { DrimainApiClient, Configuration } from '@drimain/api-client';

// TODO: Move to environment variables
const API_CONFIG = {
  basePath: 'https://api.example.com', // This should come from env vars
  // Add other configuration options as needed
};

/**
 * Pre-configured API client instance
 * TODO: Add authentication token injection, request/response interceptors
 */
export const apiClient = new DrimainApiClient(new Configuration({
  basePath: API_CONFIG.basePath,
  // TODO: Add authentication configuration
  // accessToken: () => getStoredToken(),
}));

/**
 * Authentication service methods
 * TODO: Implement proper token storage, refresh logic, error handling
 */
export const authService = {
  async login(email: string, password: string) {
    // TODO: Add proper error handling, token storage
    const response = await apiClient.login(email, password);
    return response.data;
  },

  async register(email: string, password: string, name?: string) {
    // TODO: Add proper error handling, token storage
    const response = await apiClient.register(email, password, name);
    return response.data;
  },

  async logout() {
    // TODO: Add proper token cleanup
    const response = await apiClient.logout();
    return response.data;
  },

  async refreshToken(refreshToken: string) {
    // TODO: Add proper token management
    const response = await apiClient.refreshToken(refreshToken);
    return response.data;
  }
};

/**
 * User service methods
 * TODO: Add caching, error handling, optimistic updates
 */
export const userService = {
  async getMe() {
    const response = await apiClient.getMe();
    return response.data;
  },

  async updateMe(name?: string | null) {
    const response = await apiClient.updateMe(name);
    return response.data;
  }
};

/**
 * Profile service methods
 * TODO: Add caching, error handling, optimistic updates
 */
export const profileService = {
  async getMyProfile() {
    const response = await apiClient.getMyProfile();
    return response.data;
  },

  async updateMyProfile(profile: any) {
    // TODO: Add proper typing for profile parameter
    const response = await apiClient.updateMyProfile(profile);
    return response.data;
  },

  async getProfile(userId: string) {
    const response = await apiClient.getProfile(userId);
    return response.data;
  }
};

/**
 * Health check utility
 */
export const healthService = {
  async check() {
    const response = await apiClient.healthCheck();
    return response.data;
  }
};

// TODO: Add utility functions for:
// - Token management (store, retrieve, refresh)
// - Error handling middleware
// - Request/response logging
// - Loading state management
// - Retry logic
// - Cache management