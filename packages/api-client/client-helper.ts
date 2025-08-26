/* eslint-disable */
import { OpenAPI } from './core/OpenAPI';

/**
 * Configure the API client with base URL and authentication token
 * @param baseUrl The base URL for the API (e.g., 'http://localhost:8080')
 * @param token Optional JWT token for authentication
 */
export function configureApiClient(baseUrl: string, token?: string) {
    OpenAPI.BASE = baseUrl;
    
    if (token) {
        OpenAPI.TOKEN = token;
    }
    
    // Configure default headers
    OpenAPI.HEADERS = {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` })
    };
}

/**
 * Set authentication token for subsequent API requests
 * @param token JWT token
 */
export function setAuthToken(token: string) {
    OpenAPI.TOKEN = token;
    OpenAPI.HEADERS = {
        ...OpenAPI.HEADERS,
        'Authorization': `Bearer ${token}`
    };
}

/**
 * Clear authentication token
 */
export function clearAuthToken() {
    OpenAPI.TOKEN = undefined;
    if (OpenAPI.HEADERS && OpenAPI.HEADERS['Authorization']) {
        delete OpenAPI.HEADERS['Authorization'];
    }
}

// Re-export everything from index
export * from './index';