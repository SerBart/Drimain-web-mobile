import { z } from 'zod';

/**
 * User schema for domain validation
 * This schema can be used across web and mobile applications
 */
export const UserSchema = z.object({
  id: z.number().positive(),
  username: z.string().min(3).max(50),
  email: z.string().email(),
  firstName: z.string().min(1).max(100).optional(),
  lastName: z.string().min(1).max(100).optional(),
  role: z.enum(['ADMIN', 'USER', 'MODERATOR']).default('USER'),
  isActive: z.boolean().default(true),
  createdAt: z.date().default(() => new Date()),
  updatedAt: z.date().default(() => new Date()),
});

/**
 * Type derived from UserSchema
 */
export type User = z.infer<typeof UserSchema>;

/**
 * Report schema for domain validation
 */
export const ReportSchema = z.object({
  id: z.number().positive(),
  title: z.string().min(1).max(200),
  description: z.string().optional(),
  status: z.enum(['DRAFT', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED']).default('DRAFT'),
  machineId: z.number().positive().optional(),
  assignedUserId: z.number().positive().optional(),
  createdAt: z.date().default(() => new Date()),
  updatedAt: z.date().default(() => new Date()),
});

/**
 * Type derived from ReportSchema
 */
export type Report = z.infer<typeof ReportSchema>;

// Export all schemas and types
export * from './schemas';