#!/usr/bin/env tsx

import { readFileSync, existsSync } from 'fs';
import { join, resolve } from 'path';
import yaml from 'yaml';

// Color codes for console output
const colors = {
  red: '\x1b[31m',
  green: '\x1b[32m',
  yellow: '\x1b[33m',
  blue: '\x1b[34m',
  magenta: '\x1b[35m',
  cyan: '\x1b[36m',
  reset: '\x1b[0m',
  bold: '\x1b[1m',
};

interface VerificationResult {
  passed: boolean;
  message: string;
  details?: any;
}

class ModelSyncVerifier {
  private readonly rootDir: string;
  private readonly openApiPath: string;
  private readonly coreTypesPath: string;

  constructor() {
    this.rootDir = resolve(process.cwd());
    this.openApiPath = join(
      this.rootDir,
      'packages/api-client/openapi/openapi.yaml'
    );
    this.coreTypesPath = join(this.rootDir, 'packages/core-domain/src/types');
  }

  private log(message: string, color = colors.reset): void {
    console.log(`${color}${message}${colors.reset}`);
  }

  private logSuccess(message: string): void {
    this.log(`✅ ${message}`, colors.green);
  }

  private logError(message: string): void {
    this.log(`❌ ${message}`, colors.red);
  }

  private logWarning(message: string): void {
    this.log(`⚠️  ${message}`, colors.yellow);
  }

  private logInfo(message: string): void {
    this.log(`ℹ️  ${message}`, colors.blue);
  }

  private readOpenApiSpec(): any {
    try {
      if (!existsSync(this.openApiPath)) {
        throw new Error(`OpenAPI spec not found at ${this.openApiPath}`);
      }

      const content = readFileSync(this.openApiPath, 'utf8');
      return yaml.parse(content);
    } catch (error) {
      throw new Error(`Failed to read OpenAPI spec: ${error}`);
    }
  }

  private extractUserSchemaFromOpenApi(spec: any): Record<string, any> {
    try {
      const userSchema = spec.components?.schemas?.User;
      if (!userSchema) {
        throw new Error('User schema not found in OpenAPI spec');
      }

      const properties = userSchema.properties || {};
      const requiredFields = userSchema.required || [];

      return {
        properties,
        requiredFields,
      };
    } catch (error) {
      throw new Error(`Failed to extract User schema: ${error}`);
    }
  }

  private readDomainUserType(): Record<string, any> {
    try {
      const userTypePath = join(this.coreTypesPath, 'user.ts');
      if (!existsSync(userTypePath)) {
        throw new Error(`User type file not found at ${userTypePath}`);
      }

      const content = readFileSync(userTypePath, 'utf8');

      // Extract UserSchema definition with Zod
      const userSchemaMatch = content.match(
        /export\s+const\s+UserSchema\s*=\s*z\.object\(\{([^}]+)\}\)/s
      );
      if (!userSchemaMatch) {
        throw new Error('UserSchema definition not found in user.ts');
      }

      // Extract field names from Zod schema
      const schemaContent = userSchemaMatch[1];
      const fields = schemaContent
        .split('\n')
        .map(line => line.trim())
        .filter(line => line && !line.startsWith('//'))
        .map(line => {
          const match = line.match(/(\w+):\s*z\./);
          return match ? match[1] : null;
        })
        .filter(Boolean);

      return { fields };
    } catch (error) {
      throw new Error(`Failed to read domain User type: ${error}`);
    }
  }

  private verifyUserFieldSync(): VerificationResult {
    try {
      const openApiSpec = this.readOpenApiSpec();
      const { properties: openApiProperties } =
        this.extractUserSchemaFromOpenApi(openApiSpec);
      const { fields: domainFields } = this.readDomainUserType();

      const openApiFields = Object.keys(openApiProperties);

      // Check for missing fields in domain model
      const missingInDomain = openApiFields.filter(
        field => !domainFields.includes(field)
      );
      const missingInOpenApi = domainFields.filter(
        field => !openApiFields.includes(field)
      );

      if (missingInDomain.length === 0 && missingInOpenApi.length === 0) {
        return {
          passed: true,
          message:
            'User model fields are synchronized between OpenAPI spec and domain types',
        };
      }

      const details = {
        openApiFields,
        domainFields,
        missingInDomain:
          missingInDomain.length > 0 ? missingInDomain : undefined,
        missingInOpenApi:
          missingInOpenApi.length > 0 ? missingInOpenApi : undefined,
      };

      return {
        passed: false,
        message: 'User model field mismatch detected',
        details,
      };
    } catch (error) {
      return {
        passed: false,
        message: `Error during User field verification: ${error}`,
      };
    }
  }

  private verifyFileStructure(): VerificationResult {
    const requiredFiles = [
      'packages/core-domain/src/types/user.ts',
      'packages/core-domain/src/types/auth.ts',
      'packages/core-domain/src/types/error.ts',
      'packages/api-client/openapi/openapi.yaml',
    ];

    const missingFiles = requiredFiles.filter(
      file => !existsSync(join(this.rootDir, file))
    );

    if (missingFiles.length === 0) {
      return {
        passed: true,
        message: 'All required files are present',
      };
    }

    return {
      passed: false,
      message: 'Missing required files',
      details: { missingFiles },
    };
  }

  public async run(): Promise<void> {
    this.log(
      `${colors.bold}🔍 Drimain Model Synchronization Verification${colors.reset}\n`
    );

    const verifications = [
      { name: 'File Structure', fn: () => this.verifyFileStructure() },
      { name: 'User Model Field Sync', fn: () => this.verifyUserFieldSync() },
    ];

    let allPassed = true;

    for (const verification of verifications) {
      this.logInfo(`Running ${verification.name}...`);

      try {
        const result = verification.fn();

        if (result.passed) {
          this.logSuccess(result.message);
        } else {
          this.logError(result.message);
          if (result.details) {
            console.log(JSON.stringify(result.details, null, 2));
          }
          allPassed = false;
        }
      } catch (error) {
        this.logError(`${verification.name} failed with error: ${error}`);
        allPassed = false;
      }

      console.log();
    }

    // Summary
    this.log(`${colors.bold}📋 Verification Summary${colors.reset}`);
    if (allPassed) {
      this.logSuccess('All model synchronization checks passed!');
      process.exit(0);
    } else {
      this.logError('Some model synchronization checks failed.');
      this.logWarning(
        'Please review the issues above and ensure models are synchronized.'
      );
      process.exit(1);
    }
  }
}

// Additional helper functions for future extensibility
export const addModelVerification = (
  modelName: string,
  verificationFn: () => VerificationResult
): void => {
  // This can be extended to support additional model verifications
  console.log(`Adding verification for ${modelName} model...`);
};

// Usage instructions
export const USAGE_INSTRUCTIONS = `
Model Sync Verification Usage:

1. Run verification: pnpm verify:models
2. Add new model verifications by extending the ModelSyncVerifier class
3. Update OpenAPI spec and run verification to ensure sync

Example of adding new model verification:
- Add model type to packages/core-domain/src/types/
- Add corresponding schema to packages/api-client/openapi/openapi.yaml
- Run verification to ensure sync

Future enhancements:
- Support for more complex type mappings
- Integration with TypeScript compiler API for better type parsing
- Automated fixing of simple sync issues
- Custom validation rules per model
`;

// Run if called directly
if (require.main === module) {
  const verifier = new ModelSyncVerifier();
  verifier.run().catch(error => {
    console.error(`${colors.red}Fatal error: ${error}${colors.reset}`);
    process.exit(1);
  });
}
