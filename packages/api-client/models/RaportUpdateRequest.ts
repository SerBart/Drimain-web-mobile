/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PartUsage } from './PartUsage';
export type RaportUpdateRequest = {
    /**
     * Type of repair
     */
    typNaprawy?: string;
    /**
     * Repair description
     */
    opis?: string;
    /**
     * Report status
     */
    status?: string;
    /**
     * Repair date
     */
    dataNaprawy?: string;
    /**
     * Start time
     */
    czasOd?: string;
    /**
     * End time
     */
    czasDo?: string;
    /**
     * Machine ID
     */
    maszynaId?: number;
    /**
     * Person ID
     */
    osobaId?: number;
    /**
     * Parts used in this repair
     */
    partUsages?: Array<PartUsage>;
};

