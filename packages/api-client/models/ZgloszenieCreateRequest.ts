/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { ZgloszenieStatus } from './ZgloszenieStatus';

export type ZgloszenieCreateRequest = {
    tytul: string;
    opis: string;
    status?: ZgloszenieStatus;
    /**
     * Department ID (optional, will use user's department if not specified)
     */
    dzialId?: number;
    typ?: string;
    imie?: string;
    nazwisko?: string;
};

