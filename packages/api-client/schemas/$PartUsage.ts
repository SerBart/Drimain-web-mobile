/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $PartUsage = {
    properties: {
        partId: {
            type: 'number',
            description: `ID of the used part`,
            isRequired: true,
            format: 'int64',
        },
        ilosc: {
            type: 'number',
            description: `Quantity of parts used`,
            isRequired: true,
        },
    },
} as const;
