/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $ZgloszenieCreateRequest = {
    properties: {
        typ: {
            type: 'string',
            description: `Issue type`,
            isRequired: true,
        },
        imie: {
            type: 'string',
            description: `Reporter first name`,
            isRequired: true,
        },
        nazwisko: {
            type: 'string',
            description: `Reporter last name`,
            isRequired: true,
        },
        status: {
            type: 'string',
            description: `Initial status (defaults to OPEN if not specified)`,
        },
        opis: {
            type: 'string',
            description: `Issue description`,
            isRequired: true,
        },
        photoBase64: {
            type: 'string',
            description: `Optional base64 encoded photo (future enhancement)`,
        },
    },
} as const;
