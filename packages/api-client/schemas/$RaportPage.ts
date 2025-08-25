/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export const $RaportPage = {
    properties: {
        content: {
            type: 'array',
            contains: {
                type: 'Raport',
            },
        },
        pageable: {
            properties: {
                sort: {
                    properties: {
                        sorted: {
                            type: 'boolean',
                        },
                        unsorted: {
                            type: 'boolean',
                        },
                    },
                },
                pageNumber: {
                    type: 'number',
                },
                pageSize: {
                    type: 'number',
                },
                offset: {
                    type: 'number',
                },
                unpaged: {
                    type: 'boolean',
                },
                paged: {
                    type: 'boolean',
                },
            },
        },
        totalElements: {
            type: 'number',
            format: 'int64',
        },
        totalPages: {
            type: 'number',
        },
        last: {
            type: 'boolean',
        },
        first: {
            type: 'boolean',
        },
        numberOfElements: {
            type: 'number',
        },
        size: {
            type: 'number',
        },
        number: {
            type: 'number',
        },
        sort: {
            properties: {
                sorted: {
                    type: 'boolean',
                },
                unsorted: {
                    type: 'boolean',
                },
            },
        },
        empty: {
            type: 'boolean',
        },
    },
} as const;
