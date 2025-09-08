import { ApiClient } from '../core/ApiClient';
import { 
    Part, 
    PartCreateRequest, 
    PartUpdateRequest, 
    PartQuantityPatch 
} from '../models';

export interface ListCzesciParams {
    kat?: string;
    q?: string;
    belowMin?: boolean;
}

export class CzesciApi {
    constructor(private client: ApiClient) {}

    async list(params?: ListCzesciParams): Promise<Part[]> {
        return this.client.get<Part[]>('/api/czesci', { params });
    }

    async get(id: number): Promise<Part> {
        return this.client.get<Part>(`/api/czesci/${id}`);
    }

    async create(request: PartCreateRequest): Promise<Part> {
        return this.client.post<Part>('/api/czesci', request);
    }

    async update(id: number, request: PartUpdateRequest): Promise<Part> {
        return this.client.put<Part>(`/api/czesci/${id}`, request);
    }

    async adjustQuantity(id: number, patch: PartQuantityPatch): Promise<Part> {
        return this.client.patch<Part>(`/api/czesci/${id}/ilosc`, patch);
    }

    async delete(id: number): Promise<void> {
        return this.client.delete<void>(`/api/czesci/${id}`);
    }
}