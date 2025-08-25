/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
/**
 * Mutually exclusive delta or value for part quantity adjustment.
 * Use either delta (relative change) or value (absolute quantity), but not both.
 *
 */
export type PartQuantityPatch = ({
    /**
     * Relative quantity change (positive or negative)
     */
    delta: number;
} | {
    /**
     * Absolute quantity to set
     */
    value: number;
});

