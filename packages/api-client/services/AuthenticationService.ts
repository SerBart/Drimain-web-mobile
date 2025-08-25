/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AuthRequest } from '../models/AuthRequest';
import type { AuthResponse } from '../models/AuthResponse';
import type { UserInfo } from '../models/UserInfo';
import type { CancelablePromise } from '../core/CancelablePromise';
import type { BaseHttpRequest } from '../core/BaseHttpRequest';
export class AuthenticationService {
    constructor(public readonly httpRequest: BaseHttpRequest) {}
    /**
     * User Login
     * Authenticate user with username and password, returns JWT token with expiration and roles
     * @returns AuthResponse Login successful
     * @throws ApiError
     */
    public login({
        requestBody,
    }: {
        requestBody: AuthRequest,
    }): CancelablePromise<AuthResponse> {
        return this.httpRequest.request({
            method: 'POST',
            url: '/api/auth/login',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `Invalid credentials`,
            },
        });
    }
    /**
     * Get Current User Info
     * Returns current authenticated user information including username and roles
     * @returns UserInfo Current user information
     * @throws ApiError
     */
    public getCurrentUser(): CancelablePromise<UserInfo> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/api/auth/me',
            errors: {
                401: `Invalid or missing token`,
            },
        });
    }
}
