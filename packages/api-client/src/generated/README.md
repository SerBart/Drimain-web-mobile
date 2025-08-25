## @drimain/api-client-generated@0.1.0

This generator creates TypeScript/JavaScript client that utilizes [axios](https://github.com/axios/axios). The generated Node module can be used in the following environments:

Environment
* Node.js
* Webpack
* Browserify

Language level
* ES5 - you must have a Promises/A+ library installed
* ES6

Module system
* CommonJS
* ES6 module system

It can be used in both TypeScript and JavaScript. In TypeScript, the definition will be automatically resolved via `package.json`. ([Reference](https://www.typescriptlang.org/docs/handbook/declaration-files/consumption.html))

### Building

To build and compile the typescript sources to javascript use:
```
npm install
npm run build
```

### Publishing

First build the package then run `npm publish`

### Consuming

navigate to the folder of your consuming project and run one of the following commands.

_published:_

```
npm install @drimain/api-client-generated@0.1.0 --save
```

_unPublished (not recommended):_

```
npm install PATH_TO_GENERATED_PACKAGE --save
```

### Documentation for API Endpoints

All URIs are relative to *https://api.example.com*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**authLoginPost**](docs/DefaultApi.md#authloginpost) | **POST** /auth/login | Logowanie użytkownika
*DefaultApi* | [**authLogoutPost**](docs/DefaultApi.md#authlogoutpost) | **POST** /auth/logout | Wylogowanie (unieważnienie refresh tokenu)
*DefaultApi* | [**authRefreshPost**](docs/DefaultApi.md#authrefreshpost) | **POST** /auth/refresh | Odświeżenie tokenu
*DefaultApi* | [**authRegisterPost**](docs/DefaultApi.md#authregisterpost) | **POST** /auth/register | Rejestracja użytkownika
*DefaultApi* | [**healthGet**](docs/DefaultApi.md#healthget) | **GET** /health | Health check
*DefaultApi* | [**profilesMeGet**](docs/DefaultApi.md#profilesmeget) | **GET** /profiles/me | Mój profil
*DefaultApi* | [**profilesMePatch**](docs/DefaultApi.md#profilesmepatch) | **PATCH** /profiles/me | Aktualizacja mojego profilu
*DefaultApi* | [**profilesUserIdGet**](docs/DefaultApi.md#profilesuseridget) | **GET** /profiles/{userId} | Podgląd profilu innego użytkownika
*DefaultApi* | [**usersMeGet**](docs/DefaultApi.md#usersmeget) | **GET** /users/me | Dane zalogowanego użytkownika
*DefaultApi* | [**usersMePatch**](docs/DefaultApi.md#usersmepatch) | **PATCH** /users/me | Częściowa aktualizacja danych użytkownika


### Documentation For Models

 - [AuthLoginPostRequest](docs/AuthLoginPostRequest.md)
 - [AuthRefreshPostRequest](docs/AuthRefreshPostRequest.md)
 - [AuthRegisterPost201Response](docs/AuthRegisterPost201Response.md)
 - [AuthRegisterPostRequest](docs/AuthRegisterPostRequest.md)
 - [AuthTokens](docs/AuthTokens.md)
 - [HealthGet200Response](docs/HealthGet200Response.md)
 - [NotificationPrefs](docs/NotificationPrefs.md)
 - [Profile](docs/Profile.md)
 - [ProfileUpdateInput](docs/ProfileUpdateInput.md)
 - [PublicProfile](docs/PublicProfile.md)
 - [User](docs/User.md)
 - [UserRole](docs/UserRole.md)
 - [UserStatus](docs/UserStatus.md)
 - [UsersMePatchRequest](docs/UsersMePatchRequest.md)


<a id="documentation-for-authorization"></a>
## Documentation For Authorization


Authentication schemes defined for the API:
<a id="bearerAuth"></a>
### bearerAuth

- **Type**: Bearer authentication (JWT)

