import { z } from 'zod';

// User Types
export const UserSchema = z.object({
  id: z.number(),
  username: z.string().min(3).max(50),
  email: z.string().email(),
  firstName: z.string().optional(),
  lastName: z.string().optional(),
  role: z.enum(['ADMIN', 'USER', 'OPERATOR']),
  isActive: z.boolean(),
  createdAt: z.string().datetime(),
  updatedAt: z.string().datetime(),
});

export type User = z.infer<typeof UserSchema>;

export interface CreateUserRequest {
  username: string;
  email: string;
  password: string;
  firstName?: string;
  lastName?: string;
  role?: User['role'];
}

export interface UpdateUserRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  isActive?: boolean;
}

export interface UserProfile extends Omit<User, 'createdAt' | 'updatedAt'> {
  lastLoginAt?: string;
  preferences?: {
    language: string;
    theme: 'light' | 'dark' | 'auto';
    notifications: {
      email: boolean;
      push: boolean;
    };
  };
}

// User Role Permissions
export const ROLE_PERMISSIONS = {
  ADMIN: ['*'],
  USER: ['read:own', 'write:own'],
  OPERATOR: ['read:all', 'write:parts', 'write:machines'],
} as const;

export type Permission =
  (typeof ROLE_PERMISSIONS)[keyof typeof ROLE_PERMISSIONS][number];

export const hasPermission = (
  userRole: User['role'],
  permission: string
): boolean => {
  const permissions = ROLE_PERMISSIONS[userRole] as readonly string[];
  return permissions.includes('*') || permissions.includes(permission);
};
