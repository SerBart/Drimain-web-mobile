/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Raport } from '../models/Raport';
import type { RaportCreateRequest } from '../models/RaportCreateRequest';
import type { RaportPage } from '../models/RaportPage';
import type { RaportUpdateRequest } from '../models/RaportUpdateRequest';
import type { CancelablePromise } from '../core/CancelablePromise';
import type { BaseHttpRequest } from '../core/BaseHttpRequest';
export class ReportsService {
    constructor(public readonly httpRequest: BaseHttpRequest) {}
    /**
     * List Repair Reports
     * Get paginated list of repair reports with optional filtering
     * @returns RaportPage Paginated list of repair reports
     * @throws ApiError
     */
    public listRaporty({
        status,
        maszynaId,
        from,
        to,
        q,
        page,
        size = 25,
        sort = 'dataNaprawy,desc',
    }: {
        /**
         * Filter by report status
         */
        status?: 'NOWY' | 'W_TOKU' | 'OCZEKUJE_CZESCI' | 'ZAKONCZONE' | 'ANULOWANE',
        /**
         * Filter by machine ID
         */
        maszynaId?: number,
        /**
         * Filter by date from (inclusive)
         */
        from?: string,
        /**
         * Filter by date to (inclusive)
         */
        to?: string,
        /**
         * Full-text search query
         */
        q?: string,
        /**
         * Page number (0-based)
         */
        page?: number,
        /**
         * Page size
         */
        size?: number,
        /**
         * Sorting criteria in format "field,direction" (e.g., "dataNaprawy,desc")
         */
        sort?: string,
    }): CancelablePromise<RaportPage> {
        return this.httpRequest.request({
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
        });
    }
    /**
     * Create Repair Report
     * Create a new repair report
     * @returns Raport Report created successfully
     * @throws ApiError
     */
    public createRaport({
        requestBody,
    }: {
        requestBody: RaportCreateRequest,
    }): CancelablePromise<Raport> {
        return this.httpRequest.request({
            method: 'POST',
            url: '/api/raporty',
            body: requestBody,
            mediaType: 'application/json',
        });
    }
    /**
     * Get Repair Report
     * Get a specific repair report by ID
     * @returns Raport Repair report details
     * @throws ApiError
     */
    public getRaport({
        id,
    }: {
        id: number,
    }): CancelablePromise<Raport> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/api/raporty/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Report not found`,
            },
        });
    }
    /**
     * Update Repair Report
     * Update an existing repair report
     * @returns Raport Report updated successfully
     * @throws ApiError
     */
    public updateRaport({
        id,
        requestBody,
    }: {
        id: number,
        requestBody: RaportUpdateRequest,
    }): CancelablePromise<Raport> {
        return this.httpRequest.request({
            method: 'PUT',
            url: '/api/raporty/{id}',
            path: {
                'id': id,
            },
            body: requestBody,
            mediaType: 'application/json',
            errors: {
                404: `Report not found`,
            },
        });
    }
    /**
     * Delete Repair Report
     * Delete a repair report
     * @returns void
     * @throws ApiError
     */
    public deleteRaport({
        id,
    }: {
        id: number,
    }): CancelablePromise<void> {
        return this.httpRequest.request({
            method: 'DELETE',
            url: '/api/raporty/{id}',
            path: {
                'id': id,
            },
            errors: {
                404: `Report not found`,
            },
        });
    }
}
