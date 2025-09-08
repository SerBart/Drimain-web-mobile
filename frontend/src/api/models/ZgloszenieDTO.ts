/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type ZgloszenieDTO = {
    id?: number;
    typ?: string;
    imie?: string;
    nazwisko?: string;
    status?: ZgloszenieDTO.status;
    opis?: string;
    dataGodzina?: string;
    hasPhoto?: boolean;
};
export namespace ZgloszenieDTO {
    export enum status {
        OPEN = 'OPEN',
        IN_PROGRESS = 'IN_PROGRESS',
        ON_HOLD = 'ON_HOLD',
        DONE = 'DONE',
        REJECTED = 'REJECTED',
    }
}

