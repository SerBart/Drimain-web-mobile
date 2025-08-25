/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PartUsage } from './PartUsage';
import type { SimpleMaszynaDTO } from './SimpleMaszynaDTO';
import type { SimpleOsobaDTO } from './SimpleOsobaDTO';
export type Raport = {
    id?: number;
    maszyna?: SimpleMaszynaDTO;
    /**
     * Type of repair
     */
    typNaprawy?: string;
    /**
     * Repair description
     */
    opis?: string;
    /**
     * Report status as string (enum name)
     */
    status?: string;
    /**
     * Repair date
     */
    dataNaprawy?: string;
    /**
     * Start time in HH:mm:ss or HH:mm format
     */
    czasOd?: string;
    /**
     * End time in HH:mm:ss or HH:mm format
     */
    czasDo?: string;
    osoba?: SimpleOsobaDTO;
    /**
     * Parts used in this repair
     */
    partUsages?: Array<PartUsage>;
};

