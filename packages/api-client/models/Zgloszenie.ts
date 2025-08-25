/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { ZgloszenieStatus } from './ZgloszenieStatus';
export type Zgloszenie = {
    id?: number;
    /**
     * Issue type
     */
    typ?: string;
    /**
     * Reporter first name
     */
    imie?: string;
    /**
     * Reporter last name
     */
    nazwisko?: string;
    status?: ZgloszenieStatus;
    /**
     * Issue description
     */
    opis?: string;
    /**
     * Issue timestamp in format yyyy-MM-dd'T'HH:mm (minute precision)
     */
    dataGodzina?: string;
    /**
     * Whether the issue has an associated photo
     */
    hasPhoto?: boolean;
};

