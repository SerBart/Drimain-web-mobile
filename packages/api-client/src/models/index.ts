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

export interface SimpleMaszynaDTO {
    id: number;
    nazwa: string;
}

export interface SimpleOsobaDTO {
    id: number;
    imieNazwisko: string;
}

export enum RaportStatus {
    NOWY = "NOWY",
    W_TOKU = "W_TOKU",
    OCZEKUJE_CZESCI = "OCZEKUJE_CZESCI",
    ZAKONCZONE = "ZAKONCZONE",
    ANULOWANE = "ANULOWANE"
}

export interface PartUsage {
    partId: number;
    ilosc: number;
}

export interface Raport {
    id: number;
    maszyna?: SimpleMaszynaDTO;
    typNaprawy: string;
    opis?: string;
    status: RaportStatus;
    dataNaprawy: string;
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
    status?: RaportStatus;
    dataNaprawy: string;
    czasOd?: string;
    czasDo?: string;
    partUsages?: PartUsage[];
}

export interface RaportUpdateRequest {
    maszynaId?: number;
    typNaprawy?: string;
    opis?: string;
    osobaId?: number;
    status?: RaportStatus;
    dataNaprawy?: string;
    czasOd?: string;
    czasDo?: string;
    partUsages?: PartUsage[];
}

export interface RaportPage {
    content: Raport[];
    pageable: {
        pageNumber: number;
        pageSize: number;
        sort: any;
    };
    totalElements: number;
    totalPages: number;
    last: boolean;
    first: boolean;
    numberOfElements: number;
}

export interface Part {
    id: number;
    nazwa: string;
    kod: string;
    kategoria?: string;
    ilosc: number;
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

export enum ZgloszenieStatus {
    OPEN = "OPEN",
    IN_PROGRESS = "IN_PROGRESS",
    ON_HOLD = "ON_HOLD",
    DONE = "DONE",
    REJECTED = "REJECTED"
}

export interface Zgloszenie {
    id: number;
    typ: string;
    imie: string;
    nazwisko: string;
    status: ZgloszenieStatus;
    opis: string;
    dataGodzina: string;
    hasPhoto: boolean;
}

export interface ZgloszenieCreateRequest {
    typ: string;
    imie: string;
    nazwisko: string;
    status?: ZgloszenieStatus;
    opis: string;
    dataGodzina?: string;
}

export interface ZgloszenieUpdateRequest {
    typ?: string;
    imie?: string;
    nazwisko?: string;
    status?: ZgloszenieStatus;
    opis?: string;
    dataGodzina?: string;
}

export interface ErrorResponse {
    timestamp: string;
    status: number;
    error: string;
    message: string;
    path: string;
}