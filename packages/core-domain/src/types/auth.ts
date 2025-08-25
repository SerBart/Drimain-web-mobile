import { z } from 'zod';

// Authentication Schemas
export const LoginRequestSchema = z.object({
  username: z.string().min(1, 'Username is required'),
  password: z.string().min(1, 'Password is required'),
  rememberMe: z.boolean().optional(),
});

export const LoginResponseSchema = z.object({
  accessToken: z.string(),
  refreshToken: z.string(),
  expiresIn: z.number(),
  tokenType: z.string().default('Bearer'),
  user: z.object({
    id: z.number(),
    username: z.string(),
    email: z.string().email(),
    role: z.enum(['ADMIN', 'USER', 'OPERATOR']),
  }),
});

export const RefreshTokenRequestSchema = z.object({
  refreshToken: z.string(),
});

export const RegisterRequestSchema = z
  .object({
    username: z.string().min(3).max(50),
    email: z.string().email(),
    password: z.string().min(8).max(128),
    confirmPassword: z.string(),
    firstName: z.string().optional(),
    lastName: z.string().optional(),
  })
  .refine(data => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'],
  });

export const PasswordResetRequestSchema = z.object({
  email: z.string().email(),
});

export const PasswordResetSchema = z
  .object({
    token: z.string(),
    password: z.string().min(8).max(128),
    confirmPassword: z.string(),
  })
  .refine(data => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ['confirmPassword'],
  });

// Type definitions
export type LoginRequest = z.infer<typeof LoginRequestSchema>;
export type LoginResponse = z.infer<typeof LoginResponseSchema>;
export type RefreshTokenRequest = z.infer<typeof RefreshTokenRequestSchema>;
export type RegisterRequest = z.infer<typeof RegisterRequestSchema>;
export type PasswordResetRequest = z.infer<typeof PasswordResetRequestSchema>;
export type PasswordReset = z.infer<typeof PasswordResetSchema>;

// Authentication State
export interface AuthState {
  user: LoginResponse['user'] | null;
  accessToken: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

// JWT Token payload
export interface JWTPayload {
  sub: string;
  username: string;
  role: string;
  iat: number;
  exp: number;
}

// Session management
export interface Session {
  id: string;
  userId: number;
  accessToken: string;
  refreshToken: string;
  expiresAt: Date;
  createdAt: Date;
  lastActiveAt: Date;
  userAgent?: string;
  ipAddress?: string;
}
