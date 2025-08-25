/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Part } from '../models/Part';
import type { PartCreateRequest } from '../models/PartCreateRequest';
import type { PartQuantityPatch } from '../models/PartQuantityPatch';
import type { PartUpdateRequest } from '../models/PartUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import type { BaseHttpRequest } from '../core/BaseHttpRequest';
export class PartsService {
    constructor(public readonly httpRequest: BaseHttpRequest) {}
    /**
     * List Parts
     * Get list of spare parts with optional filtering
     * @returns Part List of spare parts
     * @throws ApiError
     */
    public listParts({
        kat,
        q,
        belowMin,
    }: {
        /**
         * Filter by category
         */
        kat?: string,
        /**
         * Full-text search query (name, code, category)
         */
        q?: string,
        /**
         * Filter parts with quantity below minimum threshold
         */
        belowMin?: boolean,
    }): CancelablePromise<Array<Part>> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/api/czesci',
            query: {
                'kat': kat,
                'q': q,
                'belowMin': belowMin,
            },
        });
    }
    /**
     * Create Part
     * Create a new spare part
     * @returns Part Part created successfully
     * @throws ApiError
     */
    public createPart({
        requestBody,
    }: {
        requestBody: PartCreateRequest,
    }): CancelablePromise<Part> {
        return this.httpRequest.request({
            method: 'POST',
            url: '/api/czesci',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Get Part
     * Get a specific spare part by ID
     * @returns Part Part details
     * @throws ApiError
     */
    public getPart({
        id,
    }: {
        id: number,
    }): CancelablePromise<Part> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/api/czesci/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Part not found`,
            },
        });
    }
    /**
     * Update Part
     * Update an existing spare part
     * @returns Part Part updated successfully
     * @throws ApiError
     */
    public updatePart({
        id,
        requestBody,
    }: {
        id: number,
        requestBody: PartUpdateRequest,
    }): CancelablePromise<Part> {
        return this.httpRequest.request({
            method: 'PUT',
            url: '/api/czesci/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Part not found`,
            },
        });
    }
    /**
     * Delete Part
     * Delete a spare part
     * @returns void
     * @throws ApiError
     */
    public deletePart({
        id,
    }: {
        id: number,
    }): CancelablePromise<void> {
        return this.httpRequest.request({
            method: 'DELETE',
            url: '/api/czesci/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Part not found`,
            },
        });
    }
    /**
     * Adjust Part Quantity
     * Adjust part quantity using either relative delta or absolute value.
     * Use mutually exclusive delta (relative change) or value (absolute quantity).
     *
     * @returns Part Part quantity adjusted successfully
     * @throws ApiError
     */
    public adjustPartQuantity({
        id,
        requestBody,
    }: {
        id: number,
        requestBody: PartQuantityPatch,
    }): CancelablePromise<Part> {
        return this.httpRequest.request({
            method: 'PATCH',
            url: '/api/czesci/{id}/ilosc',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                400: `Invalid request (both delta and value specified, or neither)`,
                404: `Part not found`,
            },
        });
    }
}
