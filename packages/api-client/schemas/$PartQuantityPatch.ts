/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $PartQuantityPatch = {
    type: 'one-of',
    description: `Mutually exclusive delta or value for part quantity adjustment.
    Use either delta (relative change) or value (absolute quantity), but not both.
    `,
    contains: [{
        properties: {
            delta: {
                type: 'number',
                description: `Relative quantity change (positive or negative)`,
                isRequired: true,
            },
        },
    }, {
        properties: {
            value: {
                type: 'number',
                description: `Absolute quantity to set`,
                isRequired: true,
            },
        },
    }],
} as const;
