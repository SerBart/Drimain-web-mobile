/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PageRaportDTO } from '../models/PageRaportDTO';
import type { CancelablePromise } from '../core/CancelablePromise';
import { OpenAPI } from '../core/OpenAPI';
import { request as __request } from '../core/request';
export class RaportyService {
    /**
     * List raporty with filtering and pagination
     * @returns PageRaportDTO Page of raporty
     * @throws ApiError
     */
    public static listRaporty({
        status,
        maszynaId,
        from,
        to,
        q,
        page,
        size = 25,
        sort = 'dataNaprawy,desc',
    }: {
        status?: string,
        maszynaId?: number,
        from?: string,
        to?: string,
        q?: string,
        page?: number,
        size?: number,
        sort?: string,
    }): CancelablePromise<PageRaportDTO> {
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
        });
    }
}
