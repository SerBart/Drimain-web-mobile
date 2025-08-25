/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type AuthResponse = {
    /**
     * JWT access token
     */
    token: string;
    /**
     * Token expiration timestamp in ISO 8601 format
     */
    expiresAt: string;
    /**
     * User roles/authorities
     */
    roles: Array<string>;
};

