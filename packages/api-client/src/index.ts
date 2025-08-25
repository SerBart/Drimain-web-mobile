/**
 * @drimain/api-client - Barrel exports for easy consumption
 * 
 * Re-exports the most important functions and types from the generated client
 */

// Export the main API class and configuration
export { DefaultApi } from './generated/api';
export { Configuration } from './generated/configuration';
export type { ConfigurationParameters } from './generated/configuration';

// Export commonly used types
export type {
  User,
  UserRole,
  UserStatus,
  Profile,
  PublicProfile,
  NotificationPrefs,
  ProfileUpdateInput,
  AuthTokens,
  AuthLoginPostRequest,
  AuthRegisterPostRequest,
  AuthRefreshPostRequest,
  AuthRegisterPost201Response
} from './generated/api';

// Create convenience functions for common operations
import { DefaultApi } from './generated/api';
import { Configuration } from './generated/configuration';
import type { ConfigurationParameters } from './generated/configuration';

/**
 * Create a configured API client instance
 */
export function createApiClient(config?: ConfigurationParameters) {
  const configuration = new Configuration(config);
  return new DefaultApi(configuration);
}

/**
 * Convenience functions that wrap the most common API calls
 */
export class DrimainApiClient {
  private api: DefaultApi;

  constructor(config?: ConfigurationParameters) {
    this.api = createApiClient(config);
  }

  // Auth operations
  async login(email: string, password: string) {
    return this.api.authLoginPost({ email, password });
  }

  async register(email: string, password: string, name?: string) {
    return this.api.authRegisterPost({ email, password, name });
  }

  async refreshToken(refreshToken: string) {
    return this.api.authRefreshPost({ refreshToken });
  }

  async logout() {
    return this.api.authLogoutPost();
  }

  // User operations
  async getMe() {
    return this.api.usersMeGet();
  }

  async updateMe(name?: string | null) {
    return this.api.usersMePatch({ name });
  }

  // Profile operations
  async getMyProfile() {
    return this.api.profilesMeGet();
  }

  async updateMyProfile(profile: any) {
    return this.api.profilesMePatch(profile);
  }

  async getProfile(userId: string) {
    return this.api.profilesUserIdGet(userId);
  }

  // Health check
  async healthCheck() {
    return this.api.healthGet();
  }
}