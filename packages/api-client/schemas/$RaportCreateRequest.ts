/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RaportCreateRequest = {
    properties: {
        maszynaId: {
            type: 'number',
            description: `Machine ID`,
            format: 'int64',
        },
        typNaprawy: {
            type: 'string',
            description: `Type of repair`,
            isRequired: true,
        },
        opis: {
            type: 'string',
            description: `Repair description`,
            isRequired: true,
        },
        osobaId: {
            type: 'number',
            description: `Person ID who performed the repair`,
            format: 'int64',
        },
        status: {
            type: 'string',
            description: `Initial status (defaults to NOWY if not specified)`,
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
        partUsages: {
            type: 'array',
            contains: {
                type: 'PartUsage',
            },
        },
    },
} as const;
