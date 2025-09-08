/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ZgloszenieCreateRequest } from '../models/ZgloszenieCreateRequest';
import type { ZgloszenieDTO } from '../models/ZgloszenieDTO';
import type { ZgloszeniePage } from '../models/ZgloszeniePage';
import type { ZgloszenieUpdateRequest } from '../models/ZgloszenieUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class ZgloszeniaService {
    /**
     * List zgloszenia with optional filtering
     * @returns ZgloszeniePage List of zgloszenia
     * @throws ApiError
     */
    public static listZgloszenia({
        status,
        typ,
        q,
        page,
        size = 25,
    }: {
        status?: string,
        typ?: string,
        q?: string,
        page?: number,
        size?: number,
    }): CancelablePromise<ZgloszeniePage> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/zgloszenia',
            query: {
                'status': status,
                'typ': typ,
                'q': q,
                'page': page,
                'size': size,
            },
        });
    }
    /**
     * Create new zgloszenie
     * @returns ZgloszenieDTO Zgloszenie created successfully
     * @throws ApiError
     */
    public static createZgloszenie({
        requestBody,
    }: {
        requestBody: ZgloszenieCreateRequest,
    }): CancelablePromise<ZgloszenieDTO> {
        return __request(OpenAPI, {
            method: 'POST',
            url: '/api/zgloszenia',
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Invalid request`,
            },
        });
    }
    /**
     * Get zgloszenie by ID
     * @returns ZgloszenieDTO Zgloszenie details
     * @throws ApiError
     */
    public static getZgloszenie({
        id,
    }: {
        id: number,
    }): CancelablePromise<ZgloszenieDTO> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Zgloszenie not found`,
            },
        });
    }
    /**
     * Update existing zgloszenie
     * @returns ZgloszenieDTO Zgloszenie updated successfully
     * @throws ApiError
     */
    public static updateZgloszenie({
        id,
        requestBody,
    }: {
        id: number,
        requestBody: ZgloszenieUpdateRequest,
    }): CancelablePromise<ZgloszenieDTO> {
        return __request(OpenAPI, {
            method: 'PUT',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Invalid request`,
                404: `Zgloszenie not found`,
            },
        });
    }
    /**
     * Delete zgloszenie
     * @returns void
     * @throws ApiError
     */
    public static deleteZgloszenie({
        id,
    }: {
        id: number,
    }): CancelablePromise<void> {
        return __request(OpenAPI, {
            method: 'DELETE',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Zgloszenie not found`,
            },
        });
    }
}
