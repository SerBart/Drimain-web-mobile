import { ApiClient } from '../core/ApiClient';
import { 
    Zgloszenie, 
    ZgloszenieCreateRequest, 
    ZgloszenieUpdateRequest, 
    ZgloszenieStatus 
} from '../models';

export interface ListZgloszeniaParams {
    status?: ZgloszenieStatus;
}

export class ZgloszeniaApi {
    constructor(private client: ApiClient) {}

    async list(params?: ListZgloszeniaParams): Promise<Zgloszenie[]> {
        return this.client.get<Zgloszenie[]>('/api/zgloszenia', { params });
    }

    async get(id: number): Promise<Zgloszenie> {
        return this.client.get<Zgloszenie>(`/api/zgloszenia/${id}`);
    }

    async create(request: ZgloszenieCreateRequest): Promise<Zgloszenie> {
        return this.client.post<Zgloszenie>('/api/zgloszenia', request);
    }

    async update(id: number, request: ZgloszenieUpdateRequest): Promise<Zgloszenie> {
        return this.client.put<Zgloszenie>(`/api/zgloszenia/${id}`, request);
    }

    async delete(id: number): Promise<void> {
        return this.client.delete<void>(`/api/zgloszenia/${id}`);
    }
}