/** @type {import('@commitlint/types').UserConfig} */
module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'type-enum': [
      2,
      'always',
      [
        'feat',
        'fix',
        'docs',
        'style',
        'refactor',
        'perf',
        'test',
        'chore',
        'ci',
        'build',
        'revert'
      ]
    ],
    'scope-enum': [
      2,
      'always',
      [
        'core',
        'api',
        'web',
        'mobile',
        'tokens',
        'docs',
        'config',
        'ci',
        'deps'
      ]
    ],
    'subject-case': [2, 'never', ['start-case', 'pascal-case']],
    'subject-max-length': [2, 'always', 100]
  }
};