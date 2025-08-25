/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $AuthResponse = {
    properties: {
        token: {
            type: 'string',
            description: `JWT access token`,
            isRequired: true,
        },
        expiresAt: {
            type: 'string',
            description: `Token expiration timestamp in ISO 8601 format`,
            isRequired: true,
            format: 'date-time',
        },
        roles: {
            type: 'array',
            contains: {
                type: 'string',
            },
            isRequired: true,
        },
    },
} as const;
