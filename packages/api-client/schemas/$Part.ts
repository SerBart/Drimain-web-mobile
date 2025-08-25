/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $Part = {
    properties: {
        id: {
            type: 'number',
            format: 'int64',
        },
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
        ilosc: {
            type: 'number',
            description: `Current quantity in stock`,
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
