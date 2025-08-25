/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { Raport } from './Raport';
export type RaportPage = {
    content?: Array<Raport>;
    pageable?: {
        sort?: {
            sorted?: boolean;
            unsorted?: boolean;
        };
        pageNumber?: number;
        pageSize?: number;
        offset?: number;
        unpaged?: boolean;
        paged?: boolean;
    };
    totalElements?: number;
    totalPages?: number;
    last?: boolean;
    first?: boolean;
    numberOfElements?: number;
    size?: number;
    number?: number;
    sort?: {
        sorted?: boolean;
        unsorted?: boolean;
    };
    empty?: boolean;
};

