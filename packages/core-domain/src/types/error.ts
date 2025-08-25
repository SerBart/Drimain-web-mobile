import { z } from 'zod';

// Base Error Schema
export const ErrorSchema = z.object({
  code: z.string(),
  message: z.string(),
  details: z.any().optional(),
  timestamp: z.string().datetime().optional(),
  path: z.string().optional(),
});

export type ApiError = z.infer<typeof ErrorSchema>;

// Domain Error Classes
export class DomainError extends Error {
  public readonly code: string;
  public readonly details?: any;

  constructor(code: string, message: string, details?: any) {
    super(message);
    this.name = 'DomainError';
    this.code = code;
    this.details = details;
  }
}

export class ValidationError extends DomainError {
  constructor(message: string, details?: any) {
    super('VALIDATION_ERROR', message, details);
    this.name = 'ValidationError';
  }
}

export class NotFoundError extends DomainError {
  constructor(resource: string, id?: string | number) {
    const message = id
      ? `${resource} with id ${id} not found`
      : `${resource} not found`;
    super('NOT_FOUND', message);
    this.name = 'NotFoundError';
  }
}

export class UnauthorizedError extends DomainError {
  constructor(message: string = 'Unauthorized access') {
    super('UNAUTHORIZED', message);
    this.name = 'UnauthorizedError';
  }
}

export class ForbiddenError extends DomainError {
  constructor(message: string = 'Forbidden action') {
    super('FORBIDDEN', message);
    this.name = 'ForbiddenError';
  }
}

export class ConflictError extends DomainError {
  constructor(message: string, details?: any) {
    super('CONFLICT', message, details);
    this.name = 'ConflictError';
  }
}

export class BusinessRuleError extends DomainError {
  constructor(message: string, details?: any) {
    super('BUSINESS_RULE_VIOLATION', message, details);
    this.name = 'BusinessRuleError';
  }
}

// Error Categories for handling
export enum ErrorCategory {
  VALIDATION = 'validation',
  AUTHENTICATION = 'authentication',
  AUTHORIZATION = 'authorization',
  NOT_FOUND = 'not_found',
  CONFLICT = 'conflict',
  BUSINESS_RULE = 'business_rule',
  NETWORK = 'network',
  SERVER = 'server',
  UNKNOWN = 'unknown',
}

// Error Severity
export enum ErrorSeverity {
  LOW = 'low',
  MEDIUM = 'medium',
  HIGH = 'high',
  CRITICAL = 'critical',
}

// Error Response Format
export interface ErrorResponse {
  error: {
    code: string;
    message: string;
    category: ErrorCategory;
    severity: ErrorSeverity;
    timestamp: string;
    path?: string;
    details?: any;
    trace?: string;
  };
}

// Error Code Constants
export const ERROR_CODES = {
  // Authentication
  INVALID_CREDENTIALS: 'INVALID_CREDENTIALS',
  TOKEN_EXPIRED: 'TOKEN_EXPIRED',
  TOKEN_INVALID: 'TOKEN_INVALID',
  UNAUTHORIZED: 'UNAUTHORIZED',

  // Validation
  VALIDATION_ERROR: 'VALIDATION_ERROR',
  REQUIRED_FIELD: 'REQUIRED_FIELD',
  INVALID_FORMAT: 'INVALID_FORMAT',

  // Business Rules
  USER_ALREADY_EXISTS: 'USER_ALREADY_EXISTS',
  INVALID_OPERATION: 'INVALID_OPERATION',
  INSUFFICIENT_PERMISSIONS: 'INSUFFICIENT_PERMISSIONS',

  // System
  NOT_FOUND: 'NOT_FOUND',
  INTERNAL_ERROR: 'INTERNAL_ERROR',
  SERVICE_UNAVAILABLE: 'SERVICE_UNAVAILABLE',
} as const;

export type ErrorCode = (typeof ERROR_CODES)[keyof typeof ERROR_CODES];

// Helper function to create error responses
export const createErrorResponse = (
  code: ErrorCode,
  message: string,
  category: ErrorCategory = ErrorCategory.UNKNOWN,
  severity: ErrorSeverity = ErrorSeverity.MEDIUM,
  details?: any
): ErrorResponse => ({
  error: {
    code,
    message,
    category,
    severity,
    timestamp: new Date().toISOString(),
    details,
  },
});

// Validation helper
export const handleZodError = (error: z.ZodError): ValidationError => {
  const details = error.errors.map(err => ({
    field: err.path.join('.'),
    message: err.message,
    code: err.code,
  }));

  return new ValidationError('Validation failed', details);
};
