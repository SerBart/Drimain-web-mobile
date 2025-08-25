import { ApiClient } from '../core/ApiClient';
import { 
    Raport, 
    RaportCreateRequest, 
    RaportUpdateRequest, 
    RaportPage, 
    RaportStatus 
} from '../models';

export interface ListRaportyParams {
    page?: number;
    size?: number;
    sort?: string;
    status?: RaportStatus;
}

export class RaportyApi {
    constructor(private client: ApiClient) {}

    async list(params?: ListRaportyParams): Promise<RaportPage> {
        return this.client.get<RaportPage>('/api/raporty', { params });
    }

    async get(id: number): Promise<Raport> {
        return this.client.get<Raport>(`/api/raporty/${id}`);
    }

    async create(request: RaportCreateRequest): Promise<Raport> {
        return this.client.post<Raport>('/api/raporty', request);
    }

    async update(id: number, request: RaportUpdateRequest): Promise<Raport> {
        return this.client.put<Raport>(`/api/raporty/${id}`, request);
    }

    async delete(id: number): Promise<void> {
        return this.client.delete<void>(`/api/raporty/${id}`);
    }
}