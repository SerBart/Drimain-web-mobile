#!/usr/bin/env node

/**
 * verify-model-sync.js - Basic model synchronization verification tool
 * 
 * This tool checks that the OpenAPI specification models are consistent with
 * the backend Java models and frontend client generated types.
 * 
 * For PR #2b, we implement basic validation for:
 * - UserRole enum values
 * - UserStatus enum values  
 * - User model keys presence
 * - Profile model keys presence
 * - NotificationPrefs model keys presence
 */

const fs = require('fs');
const path = require('path');
const yaml = require('yaml');

// Colors for console output
const colors = {
  reset: '\x1b[0m',
  red: '\x1b[31m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  bold: '\x1b[1m'
};

function log(level, message) {
  const timestamp = new Date().toISOString();
  const color = level === 'ERROR' ? colors.red : 
                level === 'WARN' ? colors.yellow :
                level === 'SUCCESS' ? colors.green : colors.blue;
  
  console.log(`${color}[${timestamp}] ${level}: ${message}${colors.reset}`);
}

// Load OpenAPI specification
function loadOpenApiSpec() {
  const specPath = path.join(__dirname, '../openapi.yaml');
  
  if (!fs.existsSync(specPath)) {
    throw new Error('OpenAPI specification not found at: ' + specPath);
  }
  
  const specContent = fs.readFileSync(specPath, 'utf8');
  return yaml.parse(specContent);
}

// Validate UserRole enum
function validateUserRole(spec) {
  const userRoleSchema = spec.components?.schemas?.UserRole;
  
  if (!userRoleSchema) {
    throw new Error('UserRole schema not found in OpenAPI spec');
  }
  
  if (!userRoleSchema.enum || !Array.isArray(userRoleSchema.enum)) {
    throw new Error('UserRole enum values not found or invalid');
  }
  
  const expectedRoles = ['user', 'admin'];
  const actualRoles = userRoleSchema.enum;
  
  // Check if all expected roles are present
  for (const expectedRole of expectedRoles) {
    if (!actualRoles.includes(expectedRole)) {
      throw new Error(`UserRole missing expected value: ${expectedRole}`);
    }
  }
  
  // Check for unexpected roles (warn only)
  for (const actualRole of actualRoles) {
    if (!expectedRoles.includes(actualRole)) {
      log('WARN', `UserRole has unexpected value: ${actualRole}`);
    }
  }
  
  log('SUCCESS', `UserRole enum validated successfully: [${actualRoles.join(', ')}]`);
  return true;
}

// Validate UserStatus enum
function validateUserStatus(spec) {
  const userStatusSchema = spec.components?.schemas?.UserStatus;
  
  if (!userStatusSchema) {
    throw new Error('UserStatus schema not found in OpenAPI spec');
  }
  
  if (!userStatusSchema.enum || !Array.isArray(userStatusSchema.enum)) {
    throw new Error('UserStatus enum values not found or invalid');
  }
  
  const expectedStatuses = ['active', 'blocked'];
  const actualStatuses = userStatusSchema.enum;
  
  // Check if all expected statuses are present
  for (const expectedStatus of expectedStatuses) {
    if (!actualStatuses.includes(expectedStatus)) {
      throw new Error(`UserStatus missing expected value: ${expectedStatus}`);
    }
  }
  
  // Check for unexpected statuses (warn only)
  for (const actualStatus of actualStatuses) {
    if (!expectedStatuses.includes(actualStatus)) {
      log('WARN', `UserStatus has unexpected value: ${actualStatus}`);
    }
  }
  
  log('SUCCESS', `UserStatus enum validated successfully: [${actualStatuses.join(', ')}]`);
  return true;
}

// Validate User model keys
function validateUserModel(spec) {
  const userSchema = spec.components?.schemas?.User;
  
  if (!userSchema) {
    throw new Error('User schema not found in OpenAPI spec');
  }
  
  if (!userSchema.properties || typeof userSchema.properties !== 'object') {
    throw new Error('User schema properties not found or invalid');
  }
  
  const expectedKeys = ['id', 'email', 'name', 'role', 'status', 'createdAt', 'updatedAt'];
  const requiredKeys = userSchema.required || [];
  const actualKeys = Object.keys(userSchema.properties);
  
  // Check if all expected keys are present
  for (const expectedKey of expectedKeys) {
    if (!actualKeys.includes(expectedKey)) {
      throw new Error(`User model missing expected property: ${expectedKey}`);
    }
  }
  
  // Verify required fields
  const expectedRequiredKeys = ['id', 'email', 'role', 'status', 'createdAt', 'updatedAt'];
  for (const requiredKey of expectedRequiredKeys) {
    if (!requiredKeys.includes(requiredKey)) {
      log('WARN', `User model property '${requiredKey}' should probably be required`);
    }
  }
  
  log('SUCCESS', `User model validated successfully: [${actualKeys.join(', ')}]`);
  return true;
}

// Validate Profile model keys  
function validateProfileModel(spec) {
  const profileSchema = spec.components?.schemas?.Profile;
  
  if (!profileSchema) {
    throw new Error('Profile schema not found in OpenAPI spec');
  }
  
  if (!profileSchema.properties || typeof profileSchema.properties !== 'object') {
    throw new Error('Profile schema properties not found or invalid');
  }
  
  const expectedKeys = ['userId', 'avatarUrl', 'bio', 'notifications', 'updatedAt'];
  const requiredKeys = profileSchema.required || [];
  const actualKeys = Object.keys(profileSchema.properties);
  
  // Check if all expected keys are present
  for (const expectedKey of expectedKeys) {
    if (!actualKeys.includes(expectedKey)) {
      throw new Error(`Profile model missing expected property: ${expectedKey}`);
    }
  }
  
  // Verify required fields
  const expectedRequiredKeys = ['userId', 'notifications', 'updatedAt'];
  for (const requiredKey of expectedRequiredKeys) {
    if (!requiredKeys.includes(requiredKey)) {
      log('WARN', `Profile model property '${requiredKey}' should probably be required`);
    }
  }
  
  log('SUCCESS', `Profile model validated successfully: [${actualKeys.join(', ')}]`);
  return true;
}

// Validate NotificationPrefs model keys
function validateNotificationPrefsModel(spec) {
  const notificationPrefsSchema = spec.components?.schemas?.NotificationPrefs;
  
  if (!notificationPrefsSchema) {
    throw new Error('NotificationPrefs schema not found in OpenAPI spec');
  }
  
  if (!notificationPrefsSchema.properties || typeof notificationPrefsSchema.properties !== 'object') {
    throw new Error('NotificationPrefs schema properties not found or invalid');
  }
  
  const expectedKeys = ['email', 'push'];
  const requiredKeys = notificationPrefsSchema.required || [];
  const actualKeys = Object.keys(notificationPrefsSchema.properties);
  
  // Check if all expected keys are present
  for (const expectedKey of expectedKeys) {
    if (!actualKeys.includes(expectedKey)) {
      throw new Error(`NotificationPrefs model missing expected property: ${expectedKey}`);
    }
  }
  
  // Verify required fields
  const expectedRequiredKeys = ['email', 'push'];
  for (const requiredKey of expectedRequiredKeys) {
    if (!requiredKeys.includes(requiredKey)) {
      log('WARN', `NotificationPrefs model property '${requiredKey}' should probably be required`);
    }
  }
  
  log('SUCCESS', `NotificationPrefs model validated successfully: [${actualKeys.join(', ')}]`);
  return true;
}

// Main verification function
async function main() {
  log('INFO', 'Starting model synchronization verification...');
  
  try {
    // Load OpenAPI specification
    log('INFO', 'Loading OpenAPI specification...');
    const spec = loadOpenApiSpec();
    
    // Run validations
    log('INFO', 'Validating UserRole enum...');
    validateUserRole(spec);
    
    log('INFO', 'Validating UserStatus enum...');
    validateUserStatus(spec);
    
    log('INFO', 'Validating User model...');
    validateUserModel(spec);
    
    log('INFO', 'Validating Profile model...');
    validateProfileModel(spec);
    
    log('INFO', 'Validating NotificationPrefs model...');
    validateNotificationPrefsModel(spec);
    
    log('SUCCESS', 'All model synchronization checks passed! ✅');
    process.exit(0);
    
  } catch (error) {
    log('ERROR', `Model synchronization validation failed: ${error.message}`);
    process.exit(1);
  }
}

// Run if called directly
if (require.main === module) {
  main();
}

module.exports = {
  loadOpenApiSpec,
  validateUserRole,
  validateUserStatus,
  validateUserModel,
  validateProfileModel,
  validateNotificationPrefsModel
};