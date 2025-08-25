// Generated API client for DriMain application
// This is a placeholder file - run npm install && npm run gen:api to generate the full client

// Configuration interface for the API client
export interface ApiClientConfig {
  BASE: string;
  TOKEN?: string;
}

// Placeholder API service classes with method signatures documented as TODO
export class AuthApi {
  constructor(config: ApiClientConfig) {
    // TODO: Initialize with configuration
  }

  /**
   * Authenticate user and receive JWT token
   * POST /api/auth/login
   * TODO: Implement login method
   */
  login(request: AuthRequest): Promise<AuthResponse> {
    throw new Error('TODO: Implement login method');
  }

  /**
   * Get current user information
   * GET /api/auth/me
   * TODO: Implement me method
   */
  me(): Promise<UserInfo> {
    throw new Error('TODO: Implement me method');
  }
}

export class RaportyApi {
  constructor(config: ApiClientConfig) {
    // TODO: Initialize with configuration
  }

  /**
   * List reports with pagination
   * GET /api/raporty
   * TODO: Implement list method
   */
  list(params?: RaportListParams): Promise<RaportPage> {
    throw new Error('TODO: Implement list method');
  }

  /**
   * Create new report
   * POST /api/raporty
   * TODO: Implement create method
   */
  create(request: RaportCreateRequest): Promise<Raport> {
    throw new Error('TODO: Implement create method');
  }

  /**
   * Get report by ID
   * GET /api/raporty/{id}
   * TODO: Implement get method
   */
  get(id: number): Promise<Raport> {
    throw new Error('TODO: Implement get method');
  }

  /**
   * Update report
   * PUT /api/raporty/{id}
   * TODO: Implement update method
   */
  update(id: number, request: RaportUpdateRequest): Promise<Raport> {
    throw new Error('TODO: Implement update method');
  }

  /**
   * Delete report
   * DELETE /api/raporty/{id}
   * TODO: Implement delete method
   */
  delete(id: number): Promise<void> {
    throw new Error('TODO: Implement delete method');
  }
}

export class ZgloszeniaApi {
  constructor(config: ApiClientConfig) {
    // TODO: Initialize with configuration
  }

  /**
   * List incidents
   * GET /api/zgloszenia
   * TODO: Implement list method
   */
  list(): Promise<Zgloszenie[]> {
    throw new Error('TODO: Implement list method');
  }

  /**
   * Create new incident
   * POST /api/zgloszenia
   * TODO: Implement create method
   */
  create(request: ZgloszenieCreateRequest): Promise<Zgloszenie> {
    throw new Error('TODO: Implement create method');
  }

  /**
   * Get incident by ID
   * GET /api/zgloszenia/{id}
   * TODO: Implement get method
   */
  get(id: number): Promise<Zgloszenie> {
    throw new Error('TODO: Implement get method');
  }

  /**
   * Update incident
   * PUT /api/zgloszenia/{id}
   * TODO: Implement update method
   */
  update(id: number, request: ZgloszenieUpdateRequest): Promise<Zgloszenie> {
    throw new Error('TODO: Implement update method');
  }

  /**
   * Delete incident
   * DELETE /api/zgloszenia/{id}
   * TODO: Implement delete method
   */
  delete(id: number): Promise<void> {
    throw new Error('TODO: Implement delete method');
  }
}

export class CzesciApi {
  constructor(config: ApiClientConfig) {
    // TODO: Initialize with configuration
  }

  /**
   * List parts with optional filters
   * GET /api/czesci
   * TODO: Implement list method
   */
  list(params?: PartListParams): Promise<Part[]> {
    throw new Error('TODO: Implement list method');
  }

  /**
   * Create new part
   * POST /api/czesci
   * TODO: Implement create method
   */
  create(request: PartCreateRequest): Promise<Part> {
    throw new Error('TODO: Implement create method');
  }

  /**
   * Get part by ID
   * GET /api/czesci/{id}
   * TODO: Implement get method
   */
  get(id: number): Promise<Part> {
    throw new Error('TODO: Implement get method');
  }

  /**
   * Update part
   * PUT /api/czesci/{id}
   * TODO: Implement update method
   */
  update(id: number, request: PartUpdateRequest): Promise<Part> {
    throw new Error('TODO: Implement update method');
  }

  /**
   * Update part quantity
   * PATCH /api/czesci/{id}/ilosc
   * TODO: Implement updateQuantity method
   */
  updateQuantity(id: number, patch: PartQuantityPatch): Promise<Part> {
    throw new Error('TODO: Implement updateQuantity method');
  }

  /**
   * Delete part
   * DELETE /api/czesci/{id}
   * TODO: Implement delete method
   */
  delete(id: number): Promise<void> {
    throw new Error('TODO: Implement delete method');
  }
}

// Placeholder type interfaces for compilation
// TODO: These will be replaced by generated types

export interface AuthRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  expiresAt: string;
  roles: string[];
}

export interface UserInfo {
  username: string;
  roles: string[];
}

export interface Raport {
  id?: number;
  maszyna?: SimpleMaszynaDTO;
  typNaprawy?: string;
  opis?: string;
  status?: RaportStatus;
  dataNaprawy?: string;
  czasOd?: string;
  czasDo?: string;
  osoba?: SimpleOsobaDTO;
  partUsages?: PartUsage[];
}

export interface RaportCreateRequest {
  maszynaId?: number;
  typNaprawy: string;
  opis?: string;
  osobaId?: number;
  status?: string;
  dataNaprawy: string;
  czasOd?: string;
  czasDo?: string;
  partUsages?: PartUsage[];
}

export interface RaportUpdateRequest {
  typNaprawy?: string;
  opis?: string;
  status?: string;
  dataNaprawy?: string;
  czasOd?: string;
  czasDo?: string;
  maszynaId?: number;
  osobaId?: number;
  partUsages?: PartUsage[];
}

export interface RaportListParams {
  status?: string;
  maszynaId?: number;
  from?: string;
  to?: string;
  q?: string;
  page?: number;
  size?: number;
  sort?: string;
}

export interface RaportPage {
  content: Raport[];
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  size: number;
  number: number;
  numberOfElements: number;
}

export interface SimpleMaszynaDTO {
  id?: number;
  nazwa?: string;
}

export interface SimpleOsobaDTO {
  id?: number;
  imieNazwisko?: string;
}

export interface Part {
  id?: number;
  nazwa?: string;
  kod?: string;
  kategoria?: string;
  ilosc?: number;
  minIlosc?: number;
  jednostka?: string;
}

export interface PartCreateRequest {
  nazwa: string;
  kod: string;
  kategoria?: string;
  ilosc?: number;
  minIlosc?: number;
  jednostka?: string;
}

export interface PartUpdateRequest {
  nazwa?: string;
  kod?: string;
  kategoria?: string;
  minIlosc?: number;
  jednostka?: string;
}

export interface PartQuantityPatch {
  delta?: number;
  value?: number;
}

export interface PartUsage {
  partId?: number;
  ilosc?: number;
}

export interface PartListParams {
  kat?: string;
  q?: string;
  belowMin?: boolean;
}

export interface Zgloszenie {
  id?: number;
  typ?: string;
  imie?: string;
  nazwisko?: string;
  status?: ZgloszenieStatus;
  opis?: string;
  dataGodzina?: string;
  hasPhoto?: boolean;
}

export interface ZgloszenieCreateRequest {
  typ: string;
  imie: string;
  nazwisko: string;
  status?: string;
  opis: string;
  dataGodzina?: string;
  photoBase64?: string;
}

export interface ZgloszenieUpdateRequest {
  typ?: string;
  imie?: string;
  nazwisko?: string;
  status?: string;
  opis?: string;
  dataGodzina?: string;
  photoBase64?: string;
}

export type RaportStatus = 'NOWY' | 'W_TOKU' | 'OCZEKUJE_CZESCI' | 'ZAKONCZONE' | 'ANULOWANE';
export type ZgloszenieStatus = 'OPEN' | 'IN_PROGRESS' | 'ON_HOLD' | 'DONE' | 'REJECTED';

export interface ErrorResponse {
  error: string;
  message: string;
  timestamp: string;
  status: number;
}