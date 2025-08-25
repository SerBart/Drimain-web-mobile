const fs = require('fs');
const path = require('path');

// Read tokens from JSON file
const tokensPath = path.join(__dirname, 'tokens.json');
const tokens = JSON.parse(fs.readFileSync(tokensPath, 'utf8'));

// Generate CSS custom properties
const generateCSS = (tokenObj, prefix = '--drimain') => {
  let css = ':root {\n';

  const processTokens = (obj, currentPrefix) => {
    for (const [key, value] of Object.entries(obj)) {
      const newPrefix = `${currentPrefix}-${key}`;

      if (
        typeof value === 'object' &&
        value !== null &&
        !Array.isArray(value)
      ) {
        // Check if object has size and lineHeight (fontSize tokens)
        if (value.size && value.lineHeight) {
          css += `  ${newPrefix}-size: ${value.size};\n`;
          css += `  ${newPrefix}-line-height: ${value.lineHeight};\n`;
        } else {
          processTokens(value, newPrefix);
        }
      } else if (Array.isArray(value)) {
        css += `  ${newPrefix}: ${value.map(v => (typeof v === 'string' && v.includes(' ') ? `"${v}"` : v)).join(', ')};\n`;
      } else {
        css += `  ${newPrefix}: ${value};\n`;
      }
    }
  };

  processTokens(tokenObj, prefix);
  css += '}\n';

  return css;
};

// Generate JavaScript/TypeScript object
const generateJS = tokenObj => {
  return `export const tokens = ${JSON.stringify(tokenObj, null, 2)} as const;

export type DesignTokens = typeof tokens;

// Helper functions for accessing tokens
export const getColor = (color: string, shade?: string | number) => {
  const colorObj = tokens.colors[color as keyof typeof tokens.colors];
  if (!colorObj) return undefined;
  
  if (shade && typeof colorObj === 'object') {
    return colorObj[shade as keyof typeof colorObj];
  }
  
  return colorObj;
};

export const getSpacing = (size: string | number) => {
  return tokens.spacing[size as keyof typeof tokens.spacing];
};

export const getFontSize = (size: string) => {
  const fontSize = tokens.fontSize[size as keyof typeof tokens.fontSize];
  return typeof fontSize === 'object' ? fontSize : undefined;
};

export const getBorderRadius = (size: string) => {
  return tokens.borderRadius[size as keyof typeof tokens.borderRadius];
};

export const getBoxShadow = (size: string) => {
  return tokens.boxShadow[size as keyof typeof tokens.boxShadow];
};

// CSS variable helpers for web applications
export const cssVar = (tokenPath: string) => \`var(--drimain-\${tokenPath.replace(/\\./g, '-')})\`;

export const getCSSVar = (category: string, token: string, variant?: string | number) => {
  if (variant) {
    return cssVar(\`\${category}.\${token}.\${variant}\`);
  }
  return cssVar(\`\${category}.\${token}\`);
};

export default tokens;
`;
};

// Ensure dist directory exists
const distDir = path.join(__dirname, 'dist');
if (!fs.existsSync(distDir)) {
  fs.mkdirSync(distDir, { recursive: true });
}

// Generate and write CSS file
const cssContent = generateCSS(tokens);
fs.writeFileSync(path.join(distDir, 'tokens.css'), cssContent);

// Generate and write JavaScript file
const jsContent = generateJS(tokens);
fs.writeFileSync(path.join(distDir, 'tokens.js'), jsContent);

// Also generate TypeScript definitions
const dtsContent = `export declare const tokens: {
  colors: Record<string, Record<string, string> | string>;
  spacing: Record<string, string>;
  borderRadius: Record<string, string>;
  fontSize: Record<string, { size: string; lineHeight: string } | string>;
  fontWeight: Record<string, string>;
  fontFamily: Record<string, string[]>;
  boxShadow: Record<string, string>;
  zIndex: Record<string, string>;
  animation: {
    duration: Record<string, string>;
    easing: Record<string, string>;
  };
};

export declare type DesignTokens = typeof tokens;

export declare const getColor: (color: string, shade?: string | number) => string | undefined;
export declare const getSpacing: (size: string | number) => string | undefined;
export declare const getFontSize: (size: string) => { size: string; lineHeight: string } | undefined;
export declare const getBorderRadius: (size: string) => string | undefined;
export declare const getBoxShadow: (size: string) => string | undefined;
export declare const cssVar: (tokenPath: string) => string;
export declare const getCSSVar: (category: string, token: string, variant?: string | number) => string;

export default tokens;
`;

fs.writeFileSync(path.join(distDir, 'tokens.d.ts'), dtsContent);

console.log('✅ Design tokens generated successfully!');
console.log('📁 Generated files:');
console.log('  - dist/tokens.css (CSS custom properties)');
console.log('  - dist/tokens.js (JavaScript/TypeScript objects)');
console.log('  - dist/tokens.d.ts (TypeScript definitions)');
