package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Dto.AuthUser;
import com.vihan.Drive.Management.Dto.User;
import com.vihan.Drive.Management.Repository.AuthRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.AuthService;
import com.vihan.Drive.Management.Service.Interface.DecryptService;
import com.vihan.Drive.Management.Service.Interface.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public boolean isAuthenticated(String passwordEntered, String userId) {

        User user=userRepository.getUser(userId);
        AuthUser authUser=authRepository.getAuthUser(user);

        try {
            String decryptedPassword=decryptService.decrypt(passwordEntered, user.getDecryptionKey());
            String encryptedPassword=encryptService.encrypt(authUser.getEncryptedPassword(), user.getEncryptionKey());
            return encryptedPassword.equals(decryptedPassword);

        } catch (Exception e) {
            log.error("Authentication Failed or Not Authenticated", e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<String> generateKey(User user) {
        //id needs to be unique hash
        AuthUser authUser=AuthUser.builder()
                .id("123")
                .user(user)
                .decryptionKey(generateDecryptionKey())
                .encryptionKey(generateEncryptionKey())
                .build();

        user.setEncryptionKey(authUser.getEncryptionKey());
        user.setDecryptionKey(authUser.getDecryptionKey());

        //save user
//        userRepository.save(user);
        //save authUser
//        authRepository.save(authUser);

        return List.of(authUser.getEncryptionKey(), authUser.getDecryptionKey());
    }

    private String generateDecryptionKey() {

        UUID uniqueId = UUID.randomUUID();

        //as a precaution to avoid collision
        while(!authRepository.checkDuplicateDecryptionKey(uniqueId.toString())) {
            uniqueId = UUID.randomUUID();
        }

        return uniqueId.toString();

    }

    private String generateEncryptionKey() {
        UUID uniqueId = UUID.randomUUID();

        //as a precaution to avoid collision
        while(!authRepository.checkDuplicateEncryptionKey(uniqueId.toString())) {
            uniqueId = UUID.randomUUID();
        }

        return uniqueId.toString();
    }
}
