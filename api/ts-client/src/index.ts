// TypeScript interfaces matching OpenAPI schemas

export type ZgloszenieStatus = 'NOWE' | 'W_TRAKCIE' | 'ZAMKNIETE';

export type ErrorType = 'NOT_FOUND' | 'FORBIDDEN' | 'VALIDATION' | 'INTERNAL_ERROR';

export interface ZgloszenieCreateRequest {
  tytul?: string;
  opis: string;
  status?: ZgloszenieStatus;
}

export interface ZgloszenieUpdateRequest {
  tytul?: string;
  opis?: string;
  status?: ZgloszenieStatus;
}

export interface ZgloszenieResponse {
  id: string;
  tytul: string;
  opis: string;
  status: ZgloszenieStatus;
  createdAt: string;
  updatedAt: string;
}

export interface PageZgloszenieResponse {
  content: ZgloszenieResponse[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface ErrorResponse {
  error: ErrorType;
  message: string;
  details?: string[];
}

export interface ListZgloszeniaParams {
  page?: number;
  size?: number;
  status?: ZgloszenieStatus;
}

// API Client Error class
export class DrimainApiError extends Error {
  constructor(
    public readonly error: ErrorType,
    public readonly message: string,
    public readonly details?: string[],
    public readonly status?: number
  ) {
    super(message);
    this.name = 'DrimainApiError';
  }
}

// Main API Client class
export class DrimainApiClient {
  private readonly baseUrl: string;
  private readonly fetch: typeof fetch;

  constructor(baseUrl: string, fetchImpl: typeof fetch = globalThis.fetch) {
    this.baseUrl = baseUrl.replace(/\/+$/, ''); // Remove trailing slashes
    this.fetch = fetchImpl;
  }

  private async request<T>(
    path: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseUrl}${path}`;
    
    const defaultHeaders: Record<string, string> = {
      'Content-Type': 'application/json',
    };

    const response = await this.fetch(url, {
      ...options,
      headers: {
        ...defaultHeaders,
        ...options.headers,
      },
    });

    if (!response.ok) {
      let errorData: ErrorResponse;
      try {
        errorData = await response.json();
      } catch {
        // Fallback if response is not JSON
        errorData = {
          error: 'INTERNAL_ERROR',
          message: `HTTP ${response.status}: ${response.statusText}`,
        };
      }
      
      throw new DrimainApiError(
        errorData.error,
        errorData.message,
        errorData.details,
        response.status
      );
    }

    // Handle 204 No Content
    if (response.status === 204) {
      return undefined as T;
    }

    return response.json();
  }

  /**
   * Create a new zgłoszenie
   * @param request - The zgłoszenie creation data
   * @returns Promise resolving to the created zgłoszenie
   */
  async createZgloszenie(request: ZgloszenieCreateRequest): Promise<ZgloszenieResponse> {
    return this.request<ZgloszenieResponse>('/api/zgloszenia', {
      method: 'POST',
      body: JSON.stringify(request),
    });
  }

  /**
   * List zgłoszenia with pagination and filtering
   * @param params - Query parameters for filtering and pagination
   * @returns Promise resolving to paginated zgłoszenia list
   */
  async listZgloszenia(params: ListZgloszeniaParams = {}): Promise<PageZgloszenieResponse> {
    const searchParams = new URLSearchParams();
    
    if (params.page !== undefined) {
      searchParams.set('page', params.page.toString());
    }
    if (params.size !== undefined) {
      searchParams.set('size', params.size.toString());
    }
    if (params.status) {
      searchParams.set('status', params.status);
    }

    const query = searchParams.toString();
    const path = query ? `/api/zgloszenia?${query}` : '/api/zgloszenia';
    
    return this.request<PageZgloszenieResponse>(path);
  }

  /**
   * Get a single zgłoszenie by ID
   * @param id - The zgłoszenie UUID
   * @returns Promise resolving to the zgłoszenie details
   */
  async getZgloszenie(id: string): Promise<ZgloszenieResponse> {
    return this.request<ZgloszenieResponse>(`/api/zgloszenia/${id}`);
  }

  /**
   * Partially update a zgłoszenie
   * @param id - The zgłoszenie UUID
   * @param request - The partial update data
   * @returns Promise resolving to the updated zgłoszenie
   */
  async updateZgloszenie(
    id: string,
    request: ZgloszenieUpdateRequest
  ): Promise<ZgloszenieResponse> {
    return this.request<ZgloszenieResponse>(`/api/zgloszenia/${id}`, {
      method: 'PATCH',
      body: JSON.stringify(request),
    });
  }

  /**
   * Delete a zgłoszenie by ID
   * @param id - The zgłoszenie UUID
   * @returns Promise resolving when deletion is complete
   */
  async deleteZgloszenie(id: string): Promise<void> {
    return this.request<void>(`/api/zgloszenia/${id}`, {
      method: 'DELETE',
    });
  }
}

// Default export
export default DrimainApiClient;