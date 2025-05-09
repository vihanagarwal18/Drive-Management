package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Repository.AuthRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.AuthService;
import com.vihan.Drive.Management.Service.Interface.DecryptService;
import com.vihan.Drive.Management.Service.Interface.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final EncryptService encryptService;
    private final DecryptService decryptService;

    @Override
    @Transactional(readOnly = true)
    public boolean isAuthenticated(String passwordEntered, String userId) {
        UserModel user = userRepository.findById(userId)
                .orElse(null);

        if(user == null) {
            throw new RuntimeException("User not found: " + userId);
        }
                
        AuthUserModel authUser = authRepository.findByUser(user)
                .orElse(null);
        if (authUser == null) {
            throw new RuntimeException("Auth user not found for user: " + userId);
        }

        try {
            String decryptedPassword = decryptService.decrypt(passwordEntered, user.getDecryptionKey());
            String encryptedPassword = encryptService.encrypt(authUser.getEncryptedPassword(), user.getEncryptionKey());
            return encryptedPassword.equals(decryptedPassword);
        } catch (Exception e) {
            log.error("Authentication failed for user: " + userId, e);
            throw new RuntimeException("Authentication failed", e);
        }
    }

    @Override
    @Transactional
    public List<String> generateKey(UserModel user) {
        // Generate encryption and decryption keys
        String encryptionKey = generateEncryptionKey();
        String decryptionKey = generateDecryptionKey();

        try {
            AuthUserModel authUser = AuthUserModel.builder()
                    .user(user)
                    .encryptionKey(encryptionKey)
                    .decryptionKey(decryptionKey)
                    .encryptedPassword(encryptService.encrypt(user.getLoginDetails().getPassword(), encryptionKey))
                    .build();

            // Set keys in user model
            user.setEncryptionKey(encryptionKey);
            user.setDecryptionKey(decryptionKey);

            // Save both entities
            userRepository.save(user);
            authRepository.save(authUser);

            return List.of(encryptionKey, decryptionKey);
        } catch (Exception e) {
            log.error("Error generating keys for user: " + user.getId(), e);
            throw new RuntimeException("Failed to generate keys", e);
        }
    }

    private String generateDecryptionKey() {
        String key;
        do {
            key = UUID.randomUUID().toString();
        } while (authRepository.existsByDecryptionKey(key));
        
        return key;
    }

    private String generateEncryptionKey() {
        String key;
        do {
            key = UUID.randomUUID().toString();
        } while (authRepository.existsByEncryptionKey(key));
        
        return key;
    }
}
