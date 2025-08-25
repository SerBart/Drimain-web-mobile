/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseHttpRequest } from './core/BaseHttpRequest';
import type { OpenAPIConfig } from './core/OpenAPI';
import { FetchHttpRequest } from './core/FetchHttpRequest';
import { AuthenticationService } from './services/AuthenticationService';
import { HealthService } from './services/HealthService';
import { IssuesService } from './services/IssuesService';
import { PartsService } from './services/PartsService';
import { ReportsService } from './services/ReportsService';
type HttpRequestConstructor = new (config: OpenAPIConfig) => BaseHttpRequest;
export class DriMainApiClient {
    public readonly authentication: AuthenticationService;
    public readonly health: HealthService;
    public readonly issues: IssuesService;
    public readonly parts: PartsService;
    public readonly reports: ReportsService;
    public readonly request: BaseHttpRequest;
    constructor(config?: Partial<OpenAPIConfig>, HttpRequest: HttpRequestConstructor = FetchHttpRequest) {
        this.request = new HttpRequest({
            BASE: config?.BASE ?? 'http://localhost:8080',
            VERSION: config?.VERSION ?? '1.0.0',
            WITH_CREDENTIALS: config?.WITH_CREDENTIALS ?? false,
            CREDENTIALS: config?.CREDENTIALS ?? 'include',
            TOKEN: config?.TOKEN,
            USERNAME: config?.USERNAME,
            PASSWORD: config?.PASSWORD,
            HEADERS: config?.HEADERS,
            ENCODE_PATH: config?.ENCODE_PATH,
        });
        this.authentication = new AuthenticationService(this.request);
        this.health = new HealthService(this.request);
        this.issues = new IssuesService(this.request);
        this.parts = new PartsService(this.request);
        this.reports = new ReportsService(this.request);
    }
}

