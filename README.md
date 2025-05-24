# Drive Management System

## Tech Stack
- Backend: Spring Boot
- Database: PostgreSQL
- Storage: AWS S3
- Frontend: React
- Security: JWT, Spring Security

## Authentication and Authorization

The Drive Management System uses JWT (JSON Web Token) for authentication and authorization. All passwords are securely encrypted using BCrypt, and sensitive data is encrypted/decrypted using AES-256.

### API Endpoints

#### Authentication Endpoints

1. **Register a User**
   - **URL**: `/internal/v1/auth/register`
   - **Method**: POST
   - **Request Payload**:
     ```json
     {
       "username": "user1",
       "password": "password123",
       "name": "John Doe",
       "email": "john@example.com",
       "phoneNumber": "1234567890",
       "address": "123 Main St"
     }
     ```
   - **Response**:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
       "userId": "550e8400-e29b-41d4-a716-446655440000",
       "username": "user1",
       "message": "Registration successful"
     }
     ```
   - **Description**: Creates a new user account with encrypted password and generates encryption/decryption keys for the user.

2. **Login**
   - **URL**: `/internal/v1/auth/login`
   - **Method**: POST
   - **Request Payload**:
     ```json
     {
       "username": "user1",
       "password": "password123"
     }
     ```
   - **Response**:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
       "userId": "550e8400-e29b-41d4-a716-446655440000",
       "username": "user1",
       "message": "Login successful",
       "authType": "FORM"
     }
     ```
   - **Description**: Authenticates a user and returns a JWT token for accessing protected resources.

3. **Login with Authentication Type**
   - **URL**: `/internal/v1/auth/login/auth-type`
   - **Method**: POST
   - **Request Payload**:
     ```json
     {
       "username": "user1",
       "password": "password123",
       "authType": "BASIC_AUTH",
       "clientCertificate": null,
       "digestNonce": null
     }
     ```
   - **Authentication Types**:
     - `BASIC_AUTH`: Basic authentication
     - `FORM_AUTH`: Form-based authentication (default)
     - `CLIENT_CERT_AUTH`: Client certificate authentication
     - `DIGEST_AUTH`: Digest authentication
   - **Response**:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
       "userId": "550e8400-e29b-41d4-a716-446655440000",
       "username": "user1",
       "message": "Login successful",
       "authType": "BASIC_AUTH"
     }
     ```
   - **Description**: Authenticates a user using the specified authentication type and returns a JWT token.

3. **Validate Token**
   - **URL**: `/internal/v1/auth/validate`
   - **Method**: GET
   - **Headers** (optional if using cookies):
     ```
     Authorization: Bearer <token>
     ```
   - **Cookies** (optional if using headers):
     ```
     jwt-token=<token>
     ```
   - **Response**:
     ```json
     {
       "userId": "550e8400-e29b-41d4-a716-446655440000",
       "username": "user1",
       "valid": true,
       "expirationTime": "2023-06-01T12:00:00Z",
       "roles": ["ROLE_USER"],
       "issuedFromCookie": true,
       "authType": "FORM_AUTH"
     }
     ```
   - **Description**: Validates the JWT token (from either header or cookie) and returns detailed information about the token and the authenticated user.

4. **Authenticate with Password**
   - **URL**: `/internal/v1/auth/authenticate/{passwordEntered}/{userId}`
   - **Method**: POST
   - **Path Variables**:
     - `passwordEntered`: The password to verify
     - `userId`: The ID of the user to authenticate
   - **Response**: Boolean (true/false)
   - **Description**: Verifies if the provided password matches the stored encrypted password for the specified user.

#### Accessing Protected Endpoints

All protected endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

### Error Responses

The system provides detailed error responses for various scenarios:

1. **Invalid Credentials**:
   ```json
   {
     "message": "Invalid credentials",
     "status": 401
   }
   ```

2. **User Not Found**:
   ```json
   {
     "message": "User not found with username: user1",
     "status": 404
   }
   ```

3. **Username Already Exists**:
   ```json
   {
     "message": "Registration failed: Username already exists",
     "status": 500
   }
   ```

4. **Invalid Token**:
   ```json
   {
     "message": "Authentication failed",
     "status": 401
   }
   ```

## Security Features

1. **Password Encryption**: All passwords are encrypted using BCrypt before storage
2. **JWT Authentication**: Secure token-based authentication
3. **Data Encryption**: Sensitive data is encrypted using AES-256
4. **Unique Keys**: Each user has unique encryption and decryption keys
5. **Token Expiration**: JWT tokens expire after a configurable time period
6. **HTTP-Only Cookies**: JWT tokens are stored in HTTP-only cookies to prevent XSS attacks
7. **Refresh Tokens**: Long-lived refresh tokens for session management

## Session Management

The Drive Management System implements a robust session management system using JWT tokens stored in HTTP-only cookies alongside refresh tokens. This approach combines the benefits of JWT tokens (stateless authentication) with the security advantages of cookies (protection against XSS attacks).

### Key Features

1. **HTTP-Only Cookies for JWT Storage**
   - JWT tokens are stored in secure, HTTP-only cookies
   - Prevents JavaScript access to tokens, protecting against XSS attacks
   - Automatic token transmission with every request

2. **Refresh Token System**
   - Long-lived refresh tokens (7 days by default)
   - Stored in both cookies and database
   - Token rotation on refresh for enhanced security
   - Automatic token revocation on logout

3. **Dual Authentication Strategy**
   - JWT tokens are checked in both Authorization header and cookies
   - Maintains backward compatibility with existing clients
   - Seamless authentication flow for web applications

4. **Enhanced Security**
   - Configurable cookie settings (secure, HTTP-only, domain, path)
   - Database tracking of refresh tokens
   - Scheduled cleanup of expired tokens
   - Protection against common web vulnerabilities

### Benefits

This implementation provides several advantages:

1. **Better Security**: HTTP-only cookies protect tokens from JavaScript access
2. **Improved User Experience**: Automatic session management without client-side token storage
3. **Stateful Sessions**: Ability to track and revoke sessions when needed
4. **Cross-Site Request Protection**: Cookies can be configured with SameSite attributes

### JWT Token and Refresh Token Flow

1. **Login/Registration**: When a user logs in or registers, the system:
   - Generates a short-lived JWT token (24 hours by default)
   - Generates a long-lived refresh token (7 days by default)
   - Stores both tokens in HTTP-only cookies
   - Stores the refresh token in the database

2. **Authentication**: For each request:
   - The system checks for a JWT token in the Authorization header
   - If not found, it checks for a JWT token in the HTTP-only cookie
   - If the token is valid, the request is processed
   - If the token is expired or invalid, the request is rejected

3. **Token Refresh**: When the JWT token expires:
   - The client can call the refresh endpoint
   - The system validates the refresh token from the cookie
   - If valid, it generates a new JWT token and refresh token
   - The old refresh token is revoked
   - The new tokens are stored in HTTP-only cookies

4. **Logout**: When a user logs out:
   - The refresh token is deleted from the database
   - The cookies are cleared

### Session Endpoints

1. **Refresh Token**
   - **URL**: `/internal/v1/auth/refresh`
   - **Method**: POST
   - **Cookies Required**: `jwt-refresh-token`
   - **Response**:
     ```json
     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
       "userId": "550e8400-e29b-41d4-a716-446655440000",
       "username": "user1",
       "message": "Token refreshed successfully"
     }
     ```
   - **Description**: Refreshes the JWT token using the refresh token stored in the cookie.

2. **Logout**
   - **URL**: `/internal/v1/auth/logout`
   - **Method**: POST
   - **Cookies Required**: `jwt-refresh-token` (optional)
   - **Response**: `"Logged out successfully"`
   - **Description**: Logs out the user by revoking the refresh token and clearing cookies.

### Cookie Security

The system uses HTTP-only cookies with the following security features:

1. **HTTP-Only**: Prevents JavaScript from accessing the cookie, protecting against XSS attacks
2. **Secure** (in production): Ensures cookies are only sent over HTTPS
3. **SameSite**: Protects against CSRF attacks
4. **Path**: Restricts the cookie to specific paths
5. **Domain**: Restricts the cookie to specific domains
6. **Max-Age**: Sets the expiration time for the cookie

## File Management

The Drive Management System provides comprehensive file management capabilities, with files stored in AWS S3 and metadata stored in PostgreSQL.

### File Management Endpoints

All file endpoints require authentication with a valid JWT token in the Authorization header.

#### 1. Get File

- **URL**: `/public/v1/file/{userId}/{id}`
- **Method**: GET
- **Path Variables**:
  - `userId`: The ID of the user who owns the file
  - `id`: The ID of the file to retrieve
- **Request Parameters**:
  - `displayName`: The display name of the file
- **Request Headers**:
  - `fileType`: The type of file (IMAGE, PDF, WORD, EXCEL, POWERPOINT, TEXT, AUDIO, VIDEO)
  - `internalPath`: The internal path of the file
  - `externalPath`: The external path of the file
- **Response**: File object
  ```json
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "document.pdf",
    "displayName": "My Document",
    "fileType": "PDF",
    "internalPath": "/documents",
    "externalPath": "/user/documents",
    "s3Key": "user-id/documents/uuid-document.pdf",
    "contentType": "application/pdf",
    "size": 1024000,
    "isPublic": false
  }
  ```
- **Description**: Retrieves metadata for a specific file.

#### 2. Create File

- **URL**: `/public/v1/file/{userId}/{id}`
- **Method**: POST
- **Path Variables**:
  - `userId`: The ID of the user who will own the file
  - `id`: The ID to assign to the file (optional, will generate UUID if not provided)
- **Request Parameters**:
  - `displayName`: The display name of the file
- **Request Headers**:
  - `fileType`: The type of file (IMAGE, PDF, WORD, EXCEL, POWERPOINT, TEXT, AUDIO, VIDEO)
  - `internalPath`: The internal path of the file
  - `externalPath`: The external path of the file
- **Response**: File object (same format as Get File)
- **Description**: Creates a new file metadata entry without uploading actual file content.

#### 3. Rename File

- **URL**: `/public/v1/file/rename/{userId}/{id}`
- **Method**: PUT
- **Path Variables**:
  - `userId`: The ID of the user who owns the file
  - `id`: The ID of the file to rename
- **Request Parameters**:
  - `oldName`: The current name of the file
  - `newName`: The new name for the file
- **Request Headers**:
  - `fileType`: The type of file
  - `internalPath`: The internal path of the file
  - `externalPath`: The external path of the file
- **Response**:
  ```json
  {
    "fileName": "new-document-name.pdf",
    "userName": "550e8400-e29b-41d4-a716-446655440000",
    "oldName": "document.pdf",
    "newName": "new-document-name.pdf",
    "fileType": "PDF",
    "internalPath": "/documents",
    "externalPath": "/user/documents"
  }
  ```
- **Description**: Renames a file.

#### 4. Upload File

- **URL**: `/public/v1/file/upload/{userId}`
- **Method**: POST
- **Path Variables**:
  - `userId`: The ID of the user who will own the file
- **Request Parameters**:
  - `file`: The multipart file to upload
  - `folderPath`: The folder path to store the file (default: empty string)
  - `displayName`: The display name of the file (optional, uses original filename if not provided)
  - `isPublic`: Whether the file is publicly accessible (default: false)
- **Request Headers**:
  - `fileType`: The type of file (IMAGE, PDF, WORD, EXCEL, POWERPOINT, TEXT, AUDIO, VIDEO)
- **Response**: File object (same format as Get File)
- **Description**: Uploads a file to S3 and stores its metadata in the database.

#### 5. Download File

- **URL**: `/public/v1/file/download/{userId}/{id}`
- **Method**: GET
- **Path Variables**:
  - `userId`: The ID of the user who owns the file
  - `id`: The ID of the file to download
- **Response**: Binary file content with appropriate content type and attachment headers
- **Description**: Downloads a file from S3.

#### 6. Delete File

- **URL**: `/public/v1/file/{userId}/{id}`
- **Method**: DELETE
- **Path Variables**:
  - `userId`: The ID of the user who owns the file
  - `id`: The ID of the file to delete
- **Response**: Boolean (true if deletion was successful)
- **Description**: Deletes a file from S3 and removes its metadata from the database.

#### 7. List Files

- **URL**: `/public/v1/files/{userId}`
- **Method**: GET
- **Path Variables**:
  - `userId`: The ID of the user whose files to list
- **Request Parameters**:
  - `folderPath`: The folder path to filter files by (optional)
- **Response**: Array of File objects
  ```json
  [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "document1.pdf",
      "displayName": "My Document 1",
      "fileType": "PDF",
      "internalPath": "/documents",
      "externalPath": "/user/documents",
      "s3Key": "user-id/documents/uuid-document1.pdf",
      "contentType": "application/pdf",
      "size": 1024000,
      "isPublic": false
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "name": "image1.jpg",
      "displayName": "My Image 1",
      "fileType": "IMAGE",
      "internalPath": "/images",
      "externalPath": "/user/images",
      "s3Key": "user-id/images/uuid-image1.jpg",
      "contentType": "image/jpeg",
      "size": 2048000,
      "isPublic": true
    }
  ]
  ```
- **Description**: Lists all files for a user, optionally filtered by folder path.

### File Error Responses

The system provides detailed error responses for file operations:

1. **File Not Found**:
   ```json
   {
     "code": "FILE_NOT_FOUND",
     "message": "File not found"
   }
   ```

2. **User Not Found**:
   ```json
   {
     "code": "USER_NOT_FOUND",
     "message": "User not found with id: 550e8400-e29b-41d4-a716-446655440000"
   }
   ```

3. **Upload Failed**:
   ```json
   {
     "code": "UPLOAD_FAILED",
     "message": "Failed to upload file"
   }
   ```

4. **File Too Large**:
   ```json
   {
     "code": "FILE_TOO_LARGE",
     "message": "The uploaded file exceeds the maximum allowed size"
   }
   ```

5. **S3 File Not Found**:
   ```json
   {
     "code": "S3_FILE_NOT_FOUND",
     "message": "File not found in S3"
   }
   ```

## File Storage

Files are stored in AWS S3 with the following structure:
- S3 Key Format: `{userId}/{folderPath}/{uniqueId}-{originalFileName}`
- Example: `550e8400-e29b-41d4-a716-446655440000/documents/123e4567-e89b-12d3-a456-426614174000-document.pdf`

File metadata is stored in PostgreSQL, including:
- File ID
- Original name
- Display name
- File type
- Internal path
- External path
- S3 key
- Content type
- File size
- Public/private status
- User reference