/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Zgloszenie } from '../models/Zgloszenie';
import type { ZgloszenieCreateRequest } from '../models/ZgloszenieCreateRequest';
import type { ZgloszenieUpdateRequest } from '../models/ZgloszenieUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import type { BaseHttpRequest } from '../core/BaseHttpRequest';
export class IssuesService {
    constructor(public readonly httpRequest: BaseHttpRequest) {}
    /**
     * List Issues
     * Get list of issues with optional filtering (no pagination)
     * @returns Zgloszenie List of issues
     * @throws ApiError
     */
    public listZgloszenia({
        status,
        typ,
        q,
    }: {
        /**
         * Filter by issue status
         */
        status?: 'OPEN' | 'IN_PROGRESS' | 'ON_HOLD' | 'DONE' | 'REJECTED',
        /**
         * Filter by issue type
         */
        typ?: string,
        /**
         * Full-text search query
         */
        q?: string,
    }): CancelablePromise<Array<Zgloszenie>> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/api/zgloszenia',
            query: {
                'status': status,
                'typ': typ,
                'q': q,
            },
        });
    }
    /**
     * Create Issue
     * Create a new issue
     * @returns Zgloszenie Issue created successfully
     * @throws ApiError
     */
    public createZgloszenie({
        requestBody,
    }: {
        requestBody: ZgloszenieCreateRequest,
    }): CancelablePromise<Zgloszenie> {
        return this.httpRequest.request({
            method: 'POST',
            url: '/api/zgloszenia',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Get Issue
     * Get a specific issue by ID
     * @returns Zgloszenie Issue details
     * @throws ApiError
     */
    public getZgloszenie({
        id,
    }: {
        id: number,
    }): CancelablePromise<Zgloszenie> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Issue not found`,
            },
        });
    }
    /**
     * Update Issue
     * Update an existing issue
     * @returns Zgloszenie Issue updated successfully
     * @throws ApiError
     */
    public updateZgloszenie({
        id,
        requestBody,
    }: {
        id: number,
        requestBody: ZgloszenieUpdateRequest,
    }): CancelablePromise<Zgloszenie> {
        return this.httpRequest.request({
            method: 'PUT',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Issue not found`,
            },
        });
    }
    /**
     * Delete Issue
     * Delete an issue
     * @returns void
     * @throws ApiError
     */
    public deleteZgloszenie({
        id,
    }: {
        id: number,
    }): CancelablePromise<void> {
        return this.httpRequest.request({
            method: 'DELETE',
            url: '/api/zgloszenia/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Issue not found`,
            },
        });
    }
}
