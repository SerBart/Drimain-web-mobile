# DefaultApi

All URIs are relative to *https://api.example.com*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**authLoginPost**](#authloginpost) | **POST** /auth/login | Logowanie użytkownika|
|[**authLogoutPost**](#authlogoutpost) | **POST** /auth/logout | Wylogowanie (unieważnienie refresh tokenu)|
|[**authRefreshPost**](#authrefreshpost) | **POST** /auth/refresh | Odświeżenie tokenu|
|[**authRegisterPost**](#authregisterpost) | **POST** /auth/register | Rejestracja użytkownika|
|[**healthGet**](#healthget) | **GET** /health | Health check|
|[**profilesMeGet**](#profilesmeget) | **GET** /profiles/me | Mój profil|
|[**profilesMePatch**](#profilesmepatch) | **PATCH** /profiles/me | Aktualizacja mojego profilu|
|[**profilesUserIdGet**](#profilesuseridget) | **GET** /profiles/{userId} | Podgląd profilu innego użytkownika|
|[**usersMeGet**](#usersmeget) | **GET** /users/me | Dane zalogowanego użytkownika|
|[**usersMePatch**](#usersmepatch) | **PATCH** /users/me | Częściowa aktualizacja danych użytkownika|

# **authLoginPost**
> AuthTokens authLoginPost(authLoginPostRequest)


### Example

```typescript
import {
    DefaultApi,
    Configuration,
    AuthLoginPostRequest
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let authLoginPostRequest: AuthLoginPostRequest; //

const { status, data } = await apiInstance.authLoginPost(
    authLoginPostRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **authLoginPostRequest** | **AuthLoginPostRequest**|  | |


### Return type

**AuthTokens**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Zalogowano |  -  |
|**401** | Błędne dane logowania |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **authLogoutPost**
> authLogoutPost()


### Example

```typescript
import {
    DefaultApi,
    Configuration
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

const { status, data } = await apiInstance.authLogoutPost();
```

### Parameters
This endpoint does not have any parameters.


### Return type

void (empty response body)

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**204** | Wylogowano |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **authRefreshPost**
> AuthTokens authRefreshPost(authRefreshPostRequest)


### Example

```typescript
import {
    DefaultApi,
    Configuration,
    AuthRefreshPostRequest
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let authRefreshPostRequest: AuthRefreshPostRequest; //

const { status, data } = await apiInstance.authRefreshPost(
    authRefreshPostRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **authRefreshPostRequest** | **AuthRefreshPostRequest**|  | |


### Return type

**AuthTokens**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Nowy zestaw tokenów |  -  |
|**401** | Refresh token nieważny |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **authRegisterPost**
> AuthRegisterPost201Response authRegisterPost(authRegisterPostRequest)


### Example

```typescript
import {
    DefaultApi,
    Configuration,
    AuthRegisterPostRequest
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let authRegisterPostRequest: AuthRegisterPostRequest; //

const { status, data } = await apiInstance.authRegisterPost(
    authRegisterPostRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **authRegisterPostRequest** | **AuthRegisterPostRequest**|  | |


### Return type

**AuthRegisterPost201Response**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**201** | Utworzono użytkownika i zwrócono tokeny |  -  |
|**400** | Nieprawidłowe dane wejściowe (np. email zajęty) |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **healthGet**
> HealthGet200Response healthGet()


### Example

```typescript
import {
    DefaultApi,
    Configuration
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

const { status, data } = await apiInstance.healthGet();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**HealthGet200Response**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Serwer działa |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **profilesMeGet**
> Profile profilesMeGet()


### Example

```typescript
import {
    DefaultApi,
    Configuration
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

const { status, data } = await apiInstance.profilesMeGet();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Profile**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Profil |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **profilesMePatch**
> Profile profilesMePatch(profileUpdateInput)


### Example

```typescript
import {
    DefaultApi,
    Configuration,
    ProfileUpdateInput
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let profileUpdateInput: ProfileUpdateInput; //

const { status, data } = await apiInstance.profilesMePatch(
    profileUpdateInput
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **profileUpdateInput** | **ProfileUpdateInput**|  | |


### Return type

**Profile**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Zaktualizowano profil |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **profilesUserIdGet**
> PublicProfile profilesUserIdGet()


### Example

```typescript
import {
    DefaultApi,
    Configuration
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let userId: string; // (default to undefined)

const { status, data } = await apiInstance.profilesUserIdGet(
    userId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **userId** | [**string**] |  | defaults to undefined|


### Return type

**PublicProfile**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Profil użytkownika |  -  |
|**404** | Nie znaleziono |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **usersMeGet**
> User usersMeGet()


### Example

```typescript
import {
    DefaultApi,
    Configuration
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

const { status, data } = await apiInstance.usersMeGet();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**User**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Dane użytkownika |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **usersMePatch**
> User usersMePatch(usersMePatchRequest)


### Example

```typescript
import {
    DefaultApi,
    Configuration,
    UsersMePatchRequest
} from '@drimain/api-client-generated';

const configuration = new Configuration();
const apiInstance = new DefaultApi(configuration);

let usersMePatchRequest: UsersMePatchRequest; //

const { status, data } = await apiInstance.usersMePatch(
    usersMePatchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **usersMePatchRequest** | **UsersMePatchRequest**|  | |


### Return type

**User**

### Authorization

[bearerAuth](../README.md#bearerAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | Zaktualizowano |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

