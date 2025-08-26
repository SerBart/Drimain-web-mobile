/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { PartUsage } from './PartUsage';
import type { RaportStatus } from './RaportStatus';

export type RaportUpdateRequest = {
    typNaprawy?: string;
    opis?: string;
    status?: RaportStatus;
    dataNaprawy?: string;
    czasOd?: string;
    czasDo?: string;
    maszynaId?: number;
    osobaId?: number;
    partUsages?: Array<PartUsage>;
};

