package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Constants.AuthType;
import com.vihan.Drive.Management.Dto.AuthRequest;
import com.vihan.Drive.Management.Dto.AuthResponse;
import com.vihan.Drive.Management.Dto.AuthTypeRequest;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Dto.TokenValidationResponse;
import com.vihan.Drive.Management.Entity.RefreshTokenModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Exception.FileOperationException;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Security.CookieUtil;
import com.vihan.Drive.Management.Security.JwtUtil;
import com.vihan.Drive.Management.Service.Interface.AuthService;
import com.vihan.Drive.Management.Service.Interface.RefreshTokenService;
import com.vihan.Drive.Management.Service.Interface.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request, HttpServletResponse response) {
        // Authenticate user and get JWT token
        AuthResponse authResponse = authService.login(request);
        
        // Create refresh token
        RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(authResponse.getUserId());
        
        // Set cookies
        cookieUtil.createTokenCookie(response, authResponse.getToken());
        cookieUtil.createRefreshTokenCookie(response, refreshToken.getToken());
        
        // Set default auth type
        authResponse.setAuthType(AuthType.FORM_AUTH);
        
        return authResponse;
    }
    
    @Override
    @Transactional
    public AuthResponse loginWithAuthType(AuthTypeRequest request, HttpServletResponse response) {
        // Validate auth type
        if (request.getAuthType() == null) {
            throw new FileOperationException("Authentication type is required", "AUTH_TYPE_REQUIRED");
        }
        
        // Handle different auth types
        AuthResponse authResponse;
        
        switch (request.getAuthType()) {
            case BASIC_AUTH:
                // Basic auth implementation
                authResponse = handleBasicAuth(request, response);
                break;
                
            case CLIENT_CERT_AUTH:
                // Client certificate auth implementation
                authResponse = handleClientCertAuth(request, response);
                break;
                
            case DIGEST_AUTH:
                // Digest auth implementation
                authResponse = handleDigestAuth(request, response);
                break;
                
            case FORM_AUTH:
            default:
                // Default to form auth
                AuthRequest authRequest = new AuthRequest();
                authRequest.setUsername(request.getUsername());
                authRequest.setPassword(request.getPassword());
                authResponse = login(authRequest, response);
                break;
        }
        
        // Set the auth type in the response
        authResponse.setAuthType(request.getAuthType());
        
        return authResponse;
    }
    
    /**
     * Handle Basic Authentication
     */
    private AuthResponse handleBasicAuth(AuthTypeRequest request, HttpServletResponse response) {
        // Convert to standard auth request
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(request.getUsername());
        authRequest.setPassword(request.getPassword());
        
        // Use standard login but set different auth type
        AuthResponse authResponse = login(authRequest, response);
        authResponse.setAuthType(AuthType.BASIC_AUTH);
        
        return authResponse;
    }
    
    /**
     * Handle Client Certificate Authentication
     */
    private AuthResponse handleClientCertAuth(AuthTypeRequest request, HttpServletResponse response) {
        // In a real implementation, this would validate the client certificate
        // For now, we'll just use the username to find the user
        
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new FileOperationException("User not found", "USER_NOT_FOUND"));
            
            // Generate token
            String token = authService.generateToken(userModel.getId());
            
            // Create refresh token
            RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(userModel.getId());
            
            // Set cookies
            cookieUtil.createTokenCookie(response, token);
            cookieUtil.createRefreshTokenCookie(response, refreshToken.getToken());
            
            // Create response
            return AuthResponse.builder()
                    .token(token)
                    .userId(userModel.getId())
                    .username(userModel.getUsername())
                    .message("Client certificate authentication successful")
                    .authType(AuthType.CLIENT_CERT_AUTH)
                    .build();
                    
        } catch (Exception e) {
            log.error("Client certificate authentication failed", e);
            throw new FileOperationException("Client certificate authentication failed", "AUTH_FAILED", e);
        }
    }
    
    /**
     * Handle Digest Authentication
     */
    private AuthResponse handleDigestAuth(AuthTypeRequest request, HttpServletResponse response) {
        // In a real implementation, this would validate the digest authentication
        // For now, we'll just use the username/password
        
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername(request.getUsername());
        authRequest.setPassword(request.getPassword());
        
        // Use standard login but set different auth type
        AuthResponse authResponse = login(authRequest, response);
        authResponse.setAuthType(AuthType.DIGEST_AUTH);
        
        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request, HttpServletResponse response) {
        // Register user and get JWT token
        AuthResponse authResponse = authService.register(request);
        
        // Create refresh token
        RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(authResponse.getUserId());
        
        // Set cookies
        cookieUtil.createTokenCookie(response, authResponse.getToken());
        cookieUtil.createRefreshTokenCookie(response, refreshToken.getToken());
        
        return authResponse;
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookie
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        
        if (refreshToken == null) {
            throw new FileOperationException("Refresh token not found", "REFRESH_TOKEN_NOT_FOUND");
        }
        
        // Validate refresh token
        RefreshTokenModel tokenModel = refreshTokenService.validateRefreshToken(refreshToken)
                .orElseThrow(() -> new FileOperationException("Invalid refresh token", "INVALID_REFRESH_TOKEN"));
        
        // Generate new JWT token
        String userId = tokenModel.getUser().getId();
        String newToken = authService.generateToken(userId);
        
        // Rotate refresh token
        RefreshTokenModel newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);
        
        // Set new cookies
        cookieUtil.createTokenCookie(response, newToken);
        cookieUtil.createRefreshTokenCookie(response, newRefreshToken.getToken());
        
        // Create response
        return AuthResponse.builder()
                .token(newToken)
                .userId(userId)
                .username(tokenModel.getUser().getUsername())
                .message("Token refreshed successfully")
                .build();
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookie
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        
        // Delete refresh token if it exists
        if (refreshToken != null) {
            refreshTokenService.deleteRefreshToken(refreshToken);
        }
        
        // Clear cookies
        cookieUtil.clearTokenCookie(response);
        cookieUtil.clearRefreshTokenCookie(response);
    }
    
    @Override
    public TokenValidationResponse validateToken(HttpServletRequest request) {
        // Try to get token from header first
        String jwt = getJwtFromHeader(request);
        boolean isFromCookie = false;
        
        // If not found in header, try to get from cookie
        if (jwt == null) {
            jwt = cookieUtil.getTokenFromCookie(request);
            isFromCookie = true;
        }
        
        if (jwt == null) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
        }
        
        try {
            // Extract user ID from token
            String userId = jwtUtil.extractUsername(jwt);
            
            // Get user details
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new FileOperationException("User not found", "USER_NOT_FOUND"));
            
            // Check if token is valid
            if (!jwtUtil.validateToken(jwt)) {
                return TokenValidationResponse.builder()
                        .valid(false)
                        .build();
            }
            
            // Get token expiration time
            Date expirationDate = jwtUtil.extractExpiration(jwt);
            Instant expirationInstant = expirationDate.toInstant();
            
            // Get roles from token
            String role = jwtUtil.extractClaim(jwt, claims -> claims.get("role", String.class));
            String[] roles = role != null ? new String[]{role} : new String[0];
            
            // Determine auth type
            AuthType authType = determineAuthType(request);
            
            // Build response
            return TokenValidationResponse.builder()
                    .userId(userId)
                    .username(user.getUsername())
                    .valid(true)
                    .expirationTime(expirationInstant)
                    .roles(roles)
                    .issuedFromCookie(isFromCookie)
                    .authType(authType)
                    .build();
            
        } catch (Exception e) {
            log.error("Error validating token", e);
            return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
        }
    }
    
    /**
     * Extract JWT token from Authorization header
     */
    private String getJwtFromHeader(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        
        return null;
    }
    
    /**
     * Determine the authentication type based on the request
     */
    private AuthType determineAuthType(HttpServletRequest request) {
        // Check for Basic Auth
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            if (authHeader.startsWith("Basic ")) {
                return AuthType.BASIC_AUTH;
            } else if (authHeader.startsWith("Bearer ")) {
                return AuthType.FORM_AUTH; // JWT tokens typically come from form login
            } else if (authHeader.startsWith("Digest ")) {
                return AuthType.DIGEST_AUTH;
            }
        }
        
        // Check for client certificate
        if (request.getAttribute("javax.servlet.request.X509Certificate") != null) {
            return AuthType.CLIENT_CERT_AUTH;
        }
        
        // Default to form auth
        return AuthType.FORM_AUTH;
    }
}