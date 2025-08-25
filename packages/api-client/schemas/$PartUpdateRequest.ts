/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $PartUpdateRequest = {
    properties: {
        nazwa: {
            type: 'string',
            description: `Part name`,
        },
        kod: {
            type: 'string',
            description: `Part code/SKU`,
        },
        kategoria: {
            type: 'string',
            description: `Part category`,
        },
        minIlosc: {
            type: 'number',
            description: `Minimum quantity threshold`,
        },
        jednostka: {
            type: 'string',
            description: `Unit of measurement`,
        },
    },
} as const;
