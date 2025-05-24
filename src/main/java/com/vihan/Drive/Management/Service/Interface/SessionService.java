package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Dto.AuthRequest;
import com.vihan.Drive.Management.Dto.AuthResponse;
import com.vihan.Drive.Management.Dto.AuthTypeRequest;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Dto.TokenValidationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service for managing user sessions, including login, registration, token refresh, and logout
 */
public interface SessionService {

    /**
     * Handle user login with cookie-based session management
     * @param request The login request
     * @param response The HTTP response for setting cookies
     * @return Authentication response with user details and token
     */
    AuthResponse login(AuthRequest request, HttpServletResponse response);
    
    /**
     * Handle user login with specified authentication type
     * @param request The login request with auth type
     * @param response The HTTP response for setting cookies
     * @return Authentication response with user details and token
     */
    AuthResponse loginWithAuthType(AuthTypeRequest request, HttpServletResponse response);

    /**
     * Handle user registration with cookie-based session management
     * @param request The registration request
     * @param response The HTTP response for setting cookies
     * @return Authentication response with user details and token
     */
    AuthResponse register(RegisterRequest request, HttpServletResponse response);

    /**
     * Refresh the JWT token using the refresh token from cookies
     * @param request The HTTP request containing the refresh token cookie
     * @param response The HTTP response for setting new cookies
     * @return Authentication response with new token
     */
    AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response);

    /**
     * Log out the user by revoking the refresh token and clearing cookies
     * @param request The HTTP request containing the refresh token cookie
     * @param response The HTTP response for clearing cookies
     */
    void logout(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * Validate a JWT token and return information about it
     * @param request The HTTP request containing the token (either in header or cookie)
     * @return Token validation response with user details and expiration time
     */
    TokenValidationResponse validateToken(HttpServletRequest request);
}