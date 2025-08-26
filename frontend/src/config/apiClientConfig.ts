import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios';

// Global auth token storage
let authToken: string | null = null;

/**
 * Set the authentication token for API requests
 * @param token JWT token to use for authentication
 */
export function setAuthToken(token: string): void {
  authToken = token;
  // Ensure the interceptor is installed when setting a token
  ensureAxiosAuthInterceptor();
}

/**
 * Get the current authentication token
 * @returns Current JWT token or null if not set
 */
export function getAuthToken(): string | null {
  return authToken;
}

/**
 * Clear the authentication token
 */
export function clearAuthToken(): void {
  authToken = null;
}

// Track if interceptor is already installed
let interceptorInstalled = false;

/**
 * Ensure that the Axios authentication interceptor is installed
 * This automatically adds the Authorization header to requests when a token is set
 */
export function ensureAxiosAuthInterceptor(axiosInstance: AxiosInstance = axios): void {
  if (interceptorInstalled) {
    return;
  }

  axiosInstance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      if (authToken && config.headers) {
        config.headers.Authorization = `Bearer ${authToken}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  interceptorInstalled = true;
}

/**
 * Create a configured axios instance for API requests
 * @param baseURL Base URL for the API (optional)
 * @returns Configured axios instance
 */
export function createApiClient(baseURL?: string): AxiosInstance {
  const instance = axios.create({
    baseURL: baseURL || 'http://localhost:8080',
    timeout: 30000,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  // Install auth interceptor on this instance
  ensureAxiosAuthInterceptor(instance);

  return instance;
}

// Auto-install interceptor on default axios instance
ensureAxiosAuthInterceptor();