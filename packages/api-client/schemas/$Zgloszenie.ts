/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $Zgloszenie = {
    properties: {
        id: {
            type: 'number',
            format: 'int64',
        },
        typ: {
            type: 'string',
            description: `Issue type`,
        },
        imie: {
            type: 'string',
            description: `Reporter first name`,
        },
        nazwisko: {
            type: 'string',
            description: `Reporter last name`,
        },
        status: {
            type: 'ZgloszenieStatus',
        },
        opis: {
            type: 'string',
            description: `Issue description`,
        },
        dataGodzina: {
            type: 'string',
            description: `Issue timestamp in format yyyy-MM-dd'T'HH:mm (minute precision)`,
            format: 'date-time',
        },
        hasPhoto: {
            type: 'boolean',
            description: `Whether the issue has an associated photo`,
        },
    },
} as const;
