/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */

import type { PartUsage } from './PartUsage';
import type { RaportStatus } from './RaportStatus';
import type { SimpleMaszynaDTO } from './SimpleMaszynaDTO';
import type { SimpleOsobaDTO } from './SimpleOsobaDTO';

export type Raport = {
    id?: number;
    typNaprawy?: string;
    opis?: string;
    status?: RaportStatus;
    dataNaprawy?: string;
    czasOd?: string;
    czasDo?: string;
    maszyna?: SimpleMaszynaDTO;
    osoba?: SimpleOsobaDTO;
    partUsages?: Array<PartUsage>;
};

