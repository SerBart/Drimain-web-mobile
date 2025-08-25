import type {
  User,
  UserProfile,
  CreateUserRequest,
  UpdateUserRequest,
} from '../types/user';

// Backend DTO types (based on existing Java backend structure)
interface BackendUserDTO {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  role: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

// Mapper functions
export class UserMapper {
  /**
   * Maps backend DTO to domain User type
   */
  static fromBackend(dto: BackendUserDTO): User {
    return {
      id: dto.id,
      username: dto.username,
      email: dto.email,
      firstName: dto.firstName,
      lastName: dto.lastName,
      role: this.mapRole(dto.role),
      isActive: dto.active,
      createdAt: dto.createdAt,
      updatedAt: dto.updatedAt,
    };
  }

  /**
   * Maps domain User to backend DTO format
   */
  static toBackend(user: User): BackendUserDTO {
    return {
      id: user.id,
      username: user.username,
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
      role: user.role,
      active: user.isActive,
      createdAt: user.createdAt,
      updatedAt: user.updatedAt,
    };
  }

  /**
   * Maps CreateUserRequest to backend format
   */
  static createRequestToBackend(request: CreateUserRequest) {
    return {
      username: request.username,
      email: request.email,
      password: request.password,
      firstName: request.firstName,
      lastName: request.lastName,
      role: request.role || 'USER',
    };
  }

  /**
   * Maps UpdateUserRequest to backend format
   */
  static updateRequestToBackend(request: UpdateUserRequest) {
    return {
      firstName: request.firstName,
      lastName: request.lastName,
      email: request.email,
      active: request.isActive,
    };
  }

  /**
   * Creates UserProfile from User with additional data
   */
  static toProfile(
    user: User,
    additionalData?: Partial<UserProfile>
  ): UserProfile {
    return {
      id: user.id,
      username: user.username,
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
      role: user.role,
      isActive: user.isActive,
      ...additionalData,
    };
  }

  /**
   * Maps backend role string to domain role enum
   */
  private static mapRole(role: string): User['role'] {
    switch (role.toUpperCase()) {
      case 'ADMIN':
        return 'ADMIN';
      case 'OPERATOR':
        return 'OPERATOR';
      case 'USER':
      default:
        return 'USER';
    }
  }

  /**
   * Creates display name from user data
   */
  static getDisplayName(user: User | UserProfile): string {
    if (user.firstName && user.lastName) {
      return `${user.firstName} ${user.lastName}`;
    }
    if (user.firstName) {
      return user.firstName;
    }
    return user.username;
  }

  /**
   * Gets user initials for avatar display
   */
  static getInitials(user: User | UserProfile): string {
    if (user.firstName && user.lastName) {
      return `${user.firstName[0]}${user.lastName[0]}`.toUpperCase();
    }
    if (user.firstName) {
      return user.firstName[0]?.toUpperCase() || '';
    }
    return user.username.substring(0, 2).toUpperCase();
  }

  /**
   * Validates user data completeness
   */
  static isProfileComplete(user: User | UserProfile): boolean {
    return !!(user.email && user.firstName && user.lastName && user.isActive);
  }

  /**
   * Sanitizes user data for public display (removes sensitive info)
   */
  static toPublic(user: User): Omit<User, 'email' | 'createdAt' | 'updatedAt'> {
    const { email, createdAt, updatedAt, ...publicUser } = user;
    return publicUser;
  }
}
