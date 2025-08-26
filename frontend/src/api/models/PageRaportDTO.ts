/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PageableObject } from './PageableObject';
import type { RaportDTO } from './RaportDTO';
import type { SortObject } from './SortObject';
export type PageRaportDTO = {
    totalPages?: number;
    totalElements?: number;
    pageable?: PageableObject;
    size?: number;
    content?: Array<RaportDTO>;
    number?: number;
    sort?: Array<SortObject>;
    first?: boolean;
    last?: boolean;
    numberOfElements?: number;
    empty?: boolean;
};

