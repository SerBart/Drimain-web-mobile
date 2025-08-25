/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RaportUpdateRequest = {
    properties: {
        typNaprawy: {
            type: 'string',
            description: `Type of repair`,
        },
        opis: {
            type: 'string',
            description: `Repair description`,
        },
        status: {
            type: 'string',
            description: `Report status`,
        },
        dataNaprawy: {
            type: 'string',
            description: `Repair date`,
            format: 'date',
        },
        czasOd: {
            type: 'string',
            description: `Start time`,
            pattern: '^\\d{2}:\\d{2}(:\\d{2})?$',
        },
        czasDo: {
            type: 'string',
            description: `End time`,
            pattern: '^\\d{2}:\\d{2}(:\\d{2})?$',
        },
        maszynaId: {
            type: 'number',
            description: `Machine ID`,
            format: 'int64',
        },
        osobaId: {
            type: 'number',
            description: `Person ID`,
            format: 'int64',
        },
        partUsages: {
            type: 'array',
            contains: {
                type: 'PartUsage',
            },
        },
    },
} as const;
