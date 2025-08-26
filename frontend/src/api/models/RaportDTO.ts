/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PartUsageDTO } from './PartUsageDTO';
import type { SimpleMaszynaDTO } from './SimpleMaszynaDTO';
import type { SimpleOsobaDTO } from './SimpleOsobaDTO';
export type RaportDTO = {
    id?: number;
    maszyna?: SimpleMaszynaDTO;
    typNaprawy?: string;
    opis?: string;
    status?: string;
    dataNaprawy?: string;
    czasOd?: string;
    czasDo?: string;
    osoba?: SimpleOsobaDTO;
    partUsages?: Array<PartUsageDTO>;
};

