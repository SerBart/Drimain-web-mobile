/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type ZgloszenieCreateRequest = {
    /**
     * Issue type
     */
    typ: string;
    /**
     * Reporter first name
     */
    imie: string;
    /**
     * Reporter last name
     */
    nazwisko: string;
    /**
     * Initial status (defaults to OPEN if not specified)
     */
    status?: string;
    /**
     * Issue description
     */
    opis: string;
    /**
     * Optional base64 encoded photo (future enhancement)
     */
    photoBase64?: string;
};

