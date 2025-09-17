import { ApiClient } from '../core/ApiClient';
import { AuthRequest, AuthResponse, UserInfo } from '../models';

export class AuthApi {
    constructor(private client: ApiClient) {}

    async login(request: AuthRequest): Promise<AuthResponse> {
        return this.client.post<AuthResponse>('/api/auth/login', request);
    }

    async me(): Promise<UserInfo> {
        return this.client.get<UserInfo>('/api/auth/me');
    }
}