/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PartDTO } from '../models/PartDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class PartsService {
    /**
     * List parts with optional filtering
     * @returns PartDTO List of parts
     * @throws ApiError
     */
    public static listParts({
        kat,
        q,
        belowMin,
    }: {
        kat?: string,
        q?: string,
        belowMin?: boolean,
    }): CancelablePromise<Array<PartDTO>> {
        return __request(OpenAPI, {
            method: 'GET',
            url: '/api/czesci',
            query: {
                'kat': kat,
                'q': q,
                'belowMin': belowMin,
            },
        });
    }
}
