/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Zgloszenie } from '../models/Zgloszenie';
import type { ZgloszenieCreateRequest } from '../models/ZgloszenieCreateRequest';
import type { ZgloszeniePage } from '../models/ZgloszeniePage';
import type { ZgloszenieStatus } from '../models/ZgloszenieStatus';
import type { ZgloszenieUpdateRequest } from '../models/ZgloszenieUpdateRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class ZgOszeniaService {

    /**
     * List zgłoszenia with department-based filtering
     * Returns zgłoszenia based on user role:
     * - ADMIN/BIURO: can see all zgłoszenia
     * - Regular users: can only see zgłoszenia from their department
     *
     * @param status
     * @param page
     * @param size
     * @param sort
     * @returns ZgloszeniePage List of zgłoszenia
     * @throws ApiError
     */
    public static getApiZgloszenia(
        status?: ZgloszenieStatus,
        page?: number,
        size: number = 25,
        sort: string = 'createdAt,desc',
    ): CancelablePromise<ZgloszeniePage> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/zgloszenia',
            query: {
                'status': status,
                'page': page,
                'size': size,
                'sort': sort,
            },
            errors: {
                401: `Authentication required`,
                403: `Access denied`,
            },
        });
    }

    /**
     * Create new zgłoszenie
     * Creates a new zgłoszenie. Department assignment rules:
     * - ADMIN/BIURO: can specify dzialId or use their own department
     * - Regular users: forced to use their own department
     *
     * @param requestBody
     * @returns Zgloszenie Zgłoszenie created successfully
     * @throws ApiError
     */
    public static postApiZgloszenia(
        requestBody: ZgloszenieCreateRequest,
    ): CancelablePromise<Zgloszenie> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/zgloszenia',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request`,
                401: `Authentication required`,
                403: `Access denied`,
            },
        });
    }

    /**
     * Get zgłoszenie by ID
     * Access restricted to same department unless user has ADMIN/BIURO role
     * @param id
     * @returns Zgloszenie Zgłoszenie details
     * @throws ApiError
     */
    public static getApiZgloszenia1(
        id: number,
    ): CancelablePromise<Zgloszenie> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `Authentication required`,
                403: `Access denied`,
                404: `Resource not found`,
            },
        });
    }

    /**
     * Update zgłoszenie
     * Access restricted to same department unless user has ADMIN/BIURO role
     * @param id
     * @param requestBody
     * @returns Zgloszenie Zgłoszenie updated successfully
     * @throws ApiError
     */
    public static putApiZgloszenia(
        id: number,
        requestBody: ZgloszenieUpdateRequest,
    ): CancelablePromise<Zgloszenie> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Bad request`,
                401: `Authentication required`,
                403: `Access denied`,
                404: `Resource not found`,
            },
        });
    }

    /**
     * Delete zgłoszenie
     * Access restricted to same department unless user has ADMIN/BIURO role
     * @param id
     * @returns void
     * @throws ApiError
     */
    public static deleteApiZgloszenia(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            errors: {
                401: `Authentication required`,
                403: `Access denied`,
                404: `Resource not found`,
            },
        });
    }

}
