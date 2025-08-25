import { ApiClient, ApiConfig } from './core/ApiClient';
import { AuthApi } from './apis/AuthApi';
import { RaportyApi } from './apis/RaportyApi';
import { CzesciApi } from './apis/CzesciApi';
import { ZgloszeniaApi } from './apis/ZgloszeniaApi';

export class DriMainApiClient {
    private client: ApiClient;
    public auth: AuthApi;
    public raporty: RaportyApi;
    public czesci: CzesciApi;
    public zgloszenia: ZgloszeniaApi;

    constructor(config: ApiConfig) {
        this.client = new ApiClient(config);
        this.auth = new AuthApi(this.client);
        this.raporty = new RaportyApi(this.client);
        this.czesci = new CzesciApi(this.client);
        this.zgloszenia = new ZgloszeniaApi(this.client);
    }

    setToken(token: string): void {
        this.client.setToken(token);
    }

    clearToken(): void {
        this.client.clearToken();
    }

    async healthCheck(): Promise<string> {
        return this.client.get<string>('/health');
    }
}

/**
 * Create a new DriMain API client with the provided configuration
 * @param baseUrl - The base URL of the API
 * @param token - Optional JWT token for authentication
 * @returns DriMainApiClient instance
 */
export function createApiClient(baseUrl: string, token?: string): DriMainApiClient {
    return new DriMainApiClient({ baseUrl, token });
}

// Re-export everything from models
export * from './models';
export * from './core/ApiClient';
export * from './apis/AuthApi';
export * from './apis/RaportyApi';
export * from './apis/CzesciApi';
export * from './apis/ZgloszeniaApi';