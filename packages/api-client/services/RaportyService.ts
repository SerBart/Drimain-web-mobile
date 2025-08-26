/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Raport } from '../models/Raport';
import type { RaportCreateRequest } from '../models/RaportCreateRequest';
import type { RaportPage } from '../models/RaportPage';
import type { RaportStatus } from '../models/RaportStatus';
import type { RaportUpdateRequest } from '../models/RaportUpdateRequest';

import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';

export class RaportyService {

    /**
     * List raporty with pagination and filtering
     * @param status
     * @param maszynaId
     * @param from
     * @param to
     * @param q Search query
     * @param page
     * @param size
     * @param sort
     * @returns RaportPage List of raporty
     * @throws ApiError
     */
    public static getApiRaporty(
        status?: RaportStatus,
        maszynaId?: number,
        from?: string,
        to?: string,
        q?: string,
        page?: number,
        size: number = 25,
        sort: string = 'dataNaprawy,desc',
    ): CancelablePromise<RaportPage> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/raporty',
            query: {
                'status': status,
                'maszynaId': maszynaId,
                'from': from,
                'to': to,
                'q': q,
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
     * Create new raport
     * @param requestBody
     * @returns Raport Raport created successfully
     * @throws ApiError
     */
    public static postApiRaporty(
        requestBody: RaportCreateRequest,
    ): CancelablePromise<Raport> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/raporty',
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
     * Get raport by ID
     * @param id
     * @returns Raport Raport details
     * @throws ApiError
     */
    public static getApiRaporty1(
        id: number,
    ): CancelablePromise<Raport> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/raporty/{id}',
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
     * Update raport
     * @param id
     * @param requestBody
     * @returns Raport Raport updated successfully
     * @throws ApiError
     */
    public static putApiRaporty(
        id: number,
        requestBody: RaportUpdateRequest,
    ): CancelablePromise<Raport> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/raporty/{id}',
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
     * Delete raport
     * @param id
     * @returns void
     * @throws ApiError
     */
    public static deleteApiRaporty(
        id: number,
    ): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/raporty/{id}',
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
