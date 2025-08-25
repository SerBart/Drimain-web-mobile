# PublicProfile

Wersja profilu do odczytu dla innych użytkowników (może zostać ograniczona później)

## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**userId** | **string** |  | [default to undefined]
**avatarUrl** | **string** |  | [optional] [default to undefined]
**bio** | **string** |  | [optional] [default to undefined]
**notifications** | [**NotificationPrefs**](NotificationPrefs.md) |  | [default to undefined]
**updatedAt** | **string** |  | [default to undefined]

## Example

```typescript
import { PublicProfile } from '@drimain/api-client-generated';

const instance: PublicProfile = {
    userId,
    avatarUrl,
    bio,
    notifications,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
