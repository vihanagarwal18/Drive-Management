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
       "message": "Login successful"
     }
     ```
   - **Description**: Authenticates a user and returns a JWT token for accessing protected resources.

3. **Validate Token**
   - **URL**: `/internal/v1/auth/validate`
   - **Method**: GET
   - **Headers**:
     ```
     Authorization: Bearer <token>
     ```
   - **Response**:
     ```
     "Token is valid"
     ```
   - **Description**: Validates if the provided JWT token is valid and not expired.

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