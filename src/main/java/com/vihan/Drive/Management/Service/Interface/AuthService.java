package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Dto.AuthRequest;
import com.vihan.Drive.Management.Dto.AuthResponse;
import com.vihan.Drive.Management.Dto.RegisterRequest;

public interface AuthService {

    boolean isAuthenticated(String passwordEntered, String userId);

//    List<String> generateKey(User user);
    
    AuthResponse login(AuthRequest request);
    
    AuthResponse register(RegisterRequest request);
    
    String generateToken(String userId);

    void deleteUser(String userId);

    void forgotPassword(String username);

    void resetPassword(String token, String password);
}
