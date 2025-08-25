/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type PartCreateRequest = {
    /**
     * Part name
     */
    nazwa: string;
    /**
     * Part code/SKU
     */
    kod?: string;
    /**
     * Part category
     */
    kategoria?: string;
    /**
     * Initial quantity in stock
     */
    ilosc?: number;
    /**
     * Minimum quantity threshold
     */
    minIlosc?: number;
    /**
     * Unit of measurement
     */
    jednostka?: string;
};

