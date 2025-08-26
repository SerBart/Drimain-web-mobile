/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { AuthRequest } from '../models/AuthRequest';
import type { AuthResponse } from '../models/AuthResponse';
import type { UserInfo } from '../models/UserInfo';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class AuthenticationService {

    /**
     * Authenticate user and get JWT token
     * @param requestBody
     * @returns AuthResponse Authentication successful
     * @throws ApiError
     */
    public static postApiAuthLogin(
        requestBody: AuthRequest,
    ): CancelablePromise<AuthResponse> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/auth/login',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                401: `Authentication required`,
            },
        });
    }

    /**
     * Get current user information
     * @returns UserInfo User information retrieved successfully
     * @throws ApiError
     */
    public static getApiAuthMe(): CancelablePromise<UserInfo> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/auth/me',
            errors: {
                401: `Authentication required`,
            },
        });
    }

}
