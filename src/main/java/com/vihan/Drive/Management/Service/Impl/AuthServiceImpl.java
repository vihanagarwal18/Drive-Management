package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Dto.AuthRequest;
import com.vihan.Drive.Management.Dto.AuthResponse;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Mapper.UserMapper;
import com.vihan.Drive.Management.Repository.AuthRepository;
import com.vihan.Drive.Management.Repository.RefreshTokenRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Security.CryptoUtil;
import com.vihan.Drive.Management.Security.JwtUtil;
import com.vihan.Drive.Management.Security.PasswordService;
import com.vihan.Drive.Management.Service.Interface.AuthService;
import com.vihan.Drive.Management.Service.Interface.DecryptService;
import com.vihan.Drive.Management.Service.Interface.EncryptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EncryptService encryptService;
    private final DecryptService decryptService;
    private final JwtUtil jwtUtil;
    private final PasswordService passwordService;
    private final CryptoUtil cryptoUtil;

    @Override
    public boolean isAuthenticated(String passwordEntered, String userId) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        
        AuthUserModel authUserModel = authRepository.findByUser(userModel)
                .orElseThrow(() -> new RuntimeException("Auth user not found for user id: " + userId));

        try {
            String decryptedPassword = decryptService.decrypt(passwordEntered, userModel.getDecryptionKey());
            String encryptedPassword = encryptService.encrypt(authUserModel.getEncryptedPassword(), userModel.getEncryptionKey());
            return encryptedPassword.equals(decryptedPassword);
        } catch (Exception e) {
            log.error("Authentication Failed or Not Authenticated", e);
            throw new RuntimeException(e);
        }
    }

//    @Override
//    @Transactional
//    public List<String> generateKey(User user) {
//
//        String uniqueId = UUID.randomUUID().toString();
//
//        UserModel userModel = UserMapper.INSTANCE.toEntity(user);
//
//        String encryptionKey = generateEncryptionKey();
//        String decryptionKey = generateDecryptionKey();
//
//        userModel.setEncryptionKey(encryptionKey);
//        userModel.setDecryptionKey(decryptionKey);
//
//        AuthUserModel authUserModel = AuthUserModel.builder()
//                .id(uniqueId)
//                .user(userModel)
//                .encryptionKey(encryptionKey)
//                .decryptionKey(decryptionKey)
//                .encryptedPassword(passwordService.encryptPassword(user.getLoginDetails().password()))
//                .build();
//
//        userRepository.save(userModel);
//        authRepository.save(authUserModel);
//
//        return List.of(encryptionKey, decryptionKey);
//    }

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        try {
            UserModel userModel = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));

            AuthUserModel authUserModel = authRepository.findByUser(userModel)
                    .orElseThrow(() -> new RuntimeException("Auth user not found for username: " + request.getUsername()));

            if (!passwordService.matches(request.getPassword(), authUserModel.getEncryptedPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            String token = generateToken(userModel.getId());

            return AuthResponse.builder()
                    .token(token)
                    .userId(userModel.getId())
                    .username(userModel.getUsername())
                    .message("Login successful")
                    .build();

        } catch (Exception e) {
            log.error("Login failed", e);
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Username already exists");
            }

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            UserModel userModel = UserMapper.INSTANCE.registerRequestToEntity(request);

            String encryptionKey = generateEncryptionKey();
            String decryptionKey = generateDecryptionKey();

            userModel.setEncryptionKey(encryptionKey);
            userModel.setDecryptionKey(decryptionKey);

            userRepository.save(userModel);

            AuthUserModel authUserModel = AuthUserModel.builder()
                    .id(UUID.randomUUID().toString())
                    .user(userModel)
                    .encryptionKey(encryptionKey)
                    .decryptionKey(decryptionKey)
                    .encryptedPassword(passwordService.encryptPassword(request.getPassword()))
                    .build();
            
            authRepository.save(authUserModel);

            String token = generateToken(userModel.getId());

            return AuthResponse.builder()
                    .token(token)
                    .userId(userModel.getId())
                    .username(userModel.getUsername())
                    .message("Registration successful")
                    .build();

        } catch (Exception e) {
            log.error("Registration failed", e);
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateToken(String userId) {
        return jwtUtil.generateToken(userId, "ROLE_USER");
    }

    private String generateDecryptionKey() {
        try {
            String key = cryptoUtil.generateEncryptionKey();
            
            // As a precaution to avoid collision
            while (!authRepository.checkDuplicateDecryptionKey(key)) {
                key = cryptoUtil.generateEncryptionKey();
            }
            
            return key;
        } catch (Exception e) {
            log.error("Failed to generate decryption key", e);
            throw new RuntimeException("Failed to generate decryption key", e);
        }
    }

    private String generateEncryptionKey() {
        try {
            String key = cryptoUtil.generateEncryptionKey();
            
            // As a precaution to avoid collision
            while (!authRepository.checkDuplicateEncryptionKey(key)) {
                key = cryptoUtil.generateEncryptionKey();
            }
            
            return key;
        } catch (Exception e) {
            log.error("Failed to generate encryption key", e);
            throw new RuntimeException("Failed to generate encryption key", e);
        }
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        try {
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
            refreshTokenRepository.deleteByUser(user);
            authRepository.deleteByUserId(userId);
            userRepository.deleteById(userId);
        } catch (Exception e) {
            log.error("Failed to delete user with id: " + userId, e);
            throw new RuntimeException("Failed to delete user with id: " + userId, e);
        }
    }
}
