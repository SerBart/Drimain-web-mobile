/**
 * Smoke test for @drimain/api-client
 * Tests that the generated client can be imported and basic methods exist
 */

const { test } = require('node:test');
const { strict: assert } = require('node:assert');

// Test importing the main exports
const { 
  DefaultApi, 
  Configuration,
  DrimainApiClient,
  createApiClient
} = require('../dist/index.js');

test('API client exports exist', () => {
  assert.ok(DefaultApi, 'DefaultApi should be exported');
  assert.ok(Configuration, 'Configuration should be exported');
  assert.ok(DrimainApiClient, 'DrimainApiClient should be exported');
  assert.ok(createApiClient, 'createApiClient should be exported');
});

test('DrimainApiClient can be instantiated', () => {
  const client = new DrimainApiClient();
  assert.ok(client, 'Client should be instantiated');
  
  // Check that key methods exist
  assert.ok(typeof client.login === 'function', 'login method should exist');
  assert.ok(typeof client.register === 'function', 'register method should exist');
  assert.ok(typeof client.refreshToken === 'function', 'refreshToken method should exist');
  assert.ok(typeof client.getMe === 'function', 'getMe method should exist');
  assert.ok(typeof client.getMyProfile === 'function', 'getMyProfile method should exist');
  assert.ok(typeof client.healthCheck === 'function', 'healthCheck method should exist');
});

test('createApiClient works', () => {
  const api = createApiClient();
  assert.ok(api instanceof DefaultApi, 'Should return DefaultApi instance');
  
  // Check that the main API methods exist
  assert.ok(typeof api.authLoginPost === 'function', 'authLoginPost should exist');
  assert.ok(typeof api.authRegisterPost === 'function', 'authRegisterPost should exist');
  assert.ok(typeof api.usersMeGet === 'function', 'usersMeGet should exist');
});

console.log('✅ All smoke tests passed! API client import works correctly.');