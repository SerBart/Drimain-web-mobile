/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

export type AuthResponse = {
    /**
     * JWT token
     */
    token?: string;
    /**
     * Token expiration time
     */
    expiresAt?: string;
    roles?: Array<string>;
    /**
     * Department ID (if assigned)
     */
    deptId?: number;
    /**
     * Department name (if assigned)
     */
    deptName?: string;
};

