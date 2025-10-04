package com.vihan.Drive.Management.Controller.Internal;

import com.vihan.Drive.Management.Dto.AuthRequest;
import com.vihan.Drive.Management.Dto.AuthResponse;
import com.vihan.Drive.Management.Dto.AuthTypeRequest;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Dto.TokenValidationResponse;
import com.vihan.Drive.Management.Service.Interface.AuthService;
import com.vihan.Drive.Management.Service.Interface.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SessionService sessionService;

    @PostMapping(value = "/authenticate/{passwordEntered}/{userId}")
    public boolean isAuthenticated(
            @PathVariable(value = "passwordEntered") String passwordEntered,
            @PathVariable(value = "userId") String userId) {

        return authService.isAuthenticated(passwordEntered, userId);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        AuthResponse authResponse = sessionService.login(request, response);
        return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/login/auth-type")
    public ResponseEntity<AuthResponse> loginWithAuthType(@RequestBody AuthTypeRequest request, HttpServletResponse response) {
        AuthResponse authResponse = sessionService.loginWithAuthType(request, response);
        return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResponse authResponse = sessionService.register(request, response);
        return ResponseEntity.ok(authResponse);
    }
    
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(HttpServletRequest request) {
        TokenValidationResponse validationResponse = sessionService.validateToken(request);
        return ResponseEntity.ok(validationResponse);
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        AuthResponse authResponse = sessionService.refreshToken(request, response);
        return ResponseEntity.ok(authResponse);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        sessionService.logout(request, response);
        return ResponseEntity.ok("Logged out successfully");
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        authService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/forgot-password/{username}")
    public ResponseEntity<String> forgotPassword(@PathVariable String username) {
        try {
            String email = authService.forgotPassword(username);
            return ResponseEntity.ok("Password sent to " + email);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
