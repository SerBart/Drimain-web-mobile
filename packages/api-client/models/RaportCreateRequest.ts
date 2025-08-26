/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { PartUsage } from './PartUsage';

export type RaportCreateRequest = {
    typNaprawy: string;
    opis: string;
    dataNaprawy: string;
    czasOd?: string;
    czasDo?: string;
    maszynaId?: number;
    osobaId?: number;
    partUsages?: Array<PartUsage>;
};

