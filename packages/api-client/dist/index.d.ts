/**
 * @drimain/api-client - Barrel exports for easy consumption
 *
 * Re-exports the most important functions and types from the generated client
 */
export { DefaultApi } from './generated/api';
export { Configuration } from './generated/configuration';
export type { ConfigurationParameters } from './generated/configuration';
export type { User, UserRole, UserStatus, Profile, PublicProfile, NotificationPrefs, ProfileUpdateInput, AuthTokens, AuthLoginPostRequest, AuthRegisterPostRequest, AuthRefreshPostRequest, AuthRegisterPost201Response } from './generated/api';
import { DefaultApi } from './generated/api';
import type { ConfigurationParameters } from './generated/configuration';
/**
 * Create a configured API client instance
 */
export declare function createApiClient(config?: ConfigurationParameters): DefaultApi;
/**
 * Convenience functions that wrap the most common API calls
 */
export declare class DrimainApiClient {
    private api;
    constructor(config?: ConfigurationParameters);
    login(email: string, password: string): Promise<import("axios").AxiosResponse<import("./generated/api").AuthTokens, any>>;
    register(email: string, password: string, name?: string): Promise<import("axios").AxiosResponse<import("./generated/api").AuthRegisterPost201Response, any>>;
    refreshToken(refreshToken: string): Promise<import("axios").AxiosResponse<import("./generated/api").AuthTokens, any>>;
    logout(): Promise<import("axios").AxiosResponse<void, any>>;
    getMe(): Promise<import("axios").AxiosResponse<import("./generated/api").User, any>>;
    updateMe(name?: string | null): Promise<import("axios").AxiosResponse<import("./generated/api").User, any>>;
    getMyProfile(): Promise<import("axios").AxiosResponse<import("./generated/api").Profile, any>>;
    updateMyProfile(profile: any): Promise<import("axios").AxiosResponse<import("./generated/api").Profile, any>>;
    getProfile(userId: string): Promise<import("axios").AxiosResponse<import("./generated/api").PublicProfile, any>>;
    healthCheck(): Promise<import("axios").AxiosResponse<import("./generated/api").HealthGet200Response, any>>;
}
