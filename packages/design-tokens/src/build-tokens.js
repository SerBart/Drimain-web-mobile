const fs = require('fs');
const path = require('path');
const tokens = require('./tokens');

/**
 * Build CSS custom properties from design tokens
 */
function buildCSS() {
  let css = `/**
 * Drimain Design Tokens
 * Auto-generated CSS custom properties
 * DO NOT EDIT MANUALLY
 */

:root {
`;

  // Colors
  Object.entries(tokens.colors).forEach(([colorName, colorValues]) => {
    if (typeof colorValues === 'object') {
      Object.entries(colorValues).forEach(([shade, value]) => {
        css += `  --color-${colorName}-${shade}: ${value};\n`;
      });
    } else {
      css += `  --color-${colorName}: ${colorValues};\n`;
    }
  });

  // Spacing
  Object.entries(tokens.spacing).forEach(([key, value]) => {
    css += `  --spacing-${key}: ${value};\n`;
  });

  // Typography
  Object.entries(tokens.typography.fontFamily).forEach(([key, value]) => {
    css += `  --font-family-${key}: ${value};\n`;
  });
  
  Object.entries(tokens.typography.fontSize).forEach(([key, value]) => {
    css += `  --font-size-${key}: ${value};\n`;
  });

  Object.entries(tokens.typography.fontWeight).forEach(([key, value]) => {
    css += `  --font-weight-${key}: ${value};\n`;
  });

  // Border radius
  Object.entries(tokens.borderRadius).forEach(([key, value]) => {
    css += `  --border-radius-${key}: ${value};\n`;
  });

  // Shadows
  Object.entries(tokens.shadows).forEach(([key, value]) => {
    css += `  --shadow-${key}: ${value};\n`;
  });

  css += `}

/* Utility classes for common styling patterns */
.drimain-card {
  background: white;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-lg);
}

.drimain-button {
  background: var(--color-primary-500);
  color: white;
  border: none;
  border-radius: var(--border-radius-md);
  padding: var(--spacing-sm) var(--spacing-md);
  font-family: var(--font-family-sans);
  font-weight: var(--font-weight-medium);
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.drimain-button:hover {
  background: var(--color-primary-600);
}

.drimain-gradient-bg {
  background: linear-gradient(135deg, var(--color-primary-900), var(--color-primary-500));
}

.drimain-text-primary {
  color: var(--color-primary-500);
}

.drimain-text-secondary {
  color: var(--color-secondary-700);
}

.drimain-shadow-card {
  box-shadow: var(--shadow-lg);
}
`;

  return css;
}

// Ensure dist directory exists
const distDir = path.join(__dirname, '../dist');
if (!fs.existsSync(distDir)) {
  fs.mkdirSync(distDir, { recursive: true });
}

// Write CSS file
const cssContent = buildCSS();
fs.writeFileSync(path.join(distDir, 'tokens.css'), cssContent);
console.log('✓ Design tokens CSS generated successfully');

// Also generate JSON file for programmatic access
fs.writeFileSync(
  path.join(distDir, 'tokens.json'), 
  JSON.stringify(tokens, null, 2)
);
console.log('✓ Design tokens JSON generated successfully');