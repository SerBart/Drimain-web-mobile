/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PartUsage } from './PartUsage';
export type RaportCreateRequest = {
    /**
     * Machine ID
     */
    maszynaId?: number;
    /**
     * Type of repair
     */
    typNaprawy: string;
    /**
     * Repair description
     */
    opis: string;
    /**
     * Person ID who performed the repair
     */
    osobaId?: number;
    /**
     * Initial status (defaults to NOWY if not specified)
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
     * Parts used in this repair
     */
    partUsages?: Array<PartUsage>;
};

