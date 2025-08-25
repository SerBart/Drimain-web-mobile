/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $Raport = {
    properties: {
        id: {
            type: 'number',
            format: 'int64',
        },
        maszyna: {
            type: 'SimpleMaszynaDTO',
        },
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
            description: `Report status as string (enum name)`,
        },
        dataNaprawy: {
            type: 'string',
            description: `Repair date`,
            format: 'date',
        },
        czasOd: {
            type: 'string',
            description: `Start time in HH:mm:ss or HH:mm format`,
            pattern: '^\\d{2}:\\d{2}(:\\d{2})?$',
        },
        czasDo: {
            type: 'string',
            description: `End time in HH:mm:ss or HH:mm format`,
            pattern: '^\\d{2}:\\d{2}(:\\d{2})?$',
        },
        osoba: {
            type: 'SimpleOsobaDTO',
        },
        partUsages: {
            type: 'array',
            contains: {
                type: 'PartUsage',
            },
        },
    },
} as const;
