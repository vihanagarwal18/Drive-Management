package com.vihan.Drive.Management.Security;

import com.vihan.Drive.Management.Service.Interface.DecryptService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final DecryptService decryptService;

    public String encryptPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String decryptPassword(String encodedPassword) {
        // This is not a real decryption, it's just a placeholder.
        // In a real application, you would not be able to decrypt a password.
        // This is just for the purpose of this demo.
        return "decrypted-" + encodedPassword;
    }
}