/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { CancelablePromise } from '../core/CancelablePromise';
import type { BaseHttpRequest } from '../core/BaseHttpRequest';
export class HealthService {
    constructor(public readonly httpRequest: BaseHttpRequest) {}
    /**
     * Health Check
     * Simple health check endpoint returning service status
     * @returns any Service is healthy
     * @throws ApiError
     */
    public healthCheck(): CancelablePromise<{
        status?: string;
    }> {
        return this.httpRequest.request({
            method: 'GET',
            url: '/health',
        });
    }
}
