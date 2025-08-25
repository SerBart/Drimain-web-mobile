"use strict";
/**
 * @drimain/api-client - Barrel exports for easy consumption
 *
 * Re-exports the most important functions and types from the generated client
 */
Object.defineProperty(exports, "__esModule", { value: true });
exports.DrimainApiClient = exports.Configuration = exports.DefaultApi = void 0;
exports.createApiClient = createApiClient;
// Export the main API class and configuration
var api_1 = require("./generated/api");
Object.defineProperty(exports, "DefaultApi", { enumerable: true, get: function () { return api_1.DefaultApi; } });
var configuration_1 = require("./generated/configuration");
Object.defineProperty(exports, "Configuration", { enumerable: true, get: function () { return configuration_1.Configuration; } });
// Create convenience functions for common operations
const api_2 = require("./generated/api");
const configuration_2 = require("./generated/configuration");
/**
 * Create a configured API client instance
 */
function createApiClient(config) {
    const configuration = new configuration_2.Configuration(config);
    return new api_2.DefaultApi(configuration);
}
/**
 * Convenience functions that wrap the most common API calls
 */
class DrimainApiClient {
    constructor(config) {
        this.api = createApiClient(config);
    }
    // Auth operations
    async login(email, password) {
        return this.api.authLoginPost({ email, password });
    }
    async register(email, password, name) {
        return this.api.authRegisterPost({ email, password, name });
    }
    async refreshToken(refreshToken) {
        return this.api.authRefreshPost({ refreshToken });
    }
    async logout() {
        return this.api.authLogoutPost();
    }
    // User operations
    async getMe() {
        return this.api.usersMeGet();
    }
    async updateMe(name) {
        return this.api.usersMePatch({ name });
    }
    // Profile operations
    async getMyProfile() {
        return this.api.profilesMeGet();
    }
    async updateMyProfile(profile) {
        return this.api.profilesMePatch(profile);
    }
    async getProfile(userId) {
        return this.api.profilesUserIdGet(userId);
    }
    // Health check
    async healthCheck() {
        return this.api.healthGet();
    }
}
exports.DrimainApiClient = DrimainApiClient;
