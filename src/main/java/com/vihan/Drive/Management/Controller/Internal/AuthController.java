package com.vihan.Drive.Management.Controller.Internal;

import com.vihan.Drive.Management.Dto.AuthRequest;
import com.vihan.Drive.Management.Dto.AuthResponse;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Service.Interface.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/v1/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/authenticate/{passwordEntered}/{userId}")
    public boolean isAuthenticated(
            @PathVariable(value = "passwordEntered") String passwordEntered,
            @PathVariable(value = "userId") String userId) {

        return authService.isAuthenticated(passwordEntered, userId);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken() {

        // This endpoint will be protected by JWT authentication
        // If the request reaches here, it means the token is valid
        return ResponseEntity.ok("Token is valid");
    }
}
