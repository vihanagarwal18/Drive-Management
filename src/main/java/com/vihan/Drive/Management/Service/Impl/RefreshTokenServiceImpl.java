package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Entity.RefreshTokenModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Exception.FileOperationException;
import com.vihan.Drive.Management.Repository.RefreshTokenRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.expiration:604800000}") // 7 days in milliseconds
    private Long refreshTokenDurationMs;

    @Override
    @Transactional
    public RefreshTokenModel createRefreshToken(String userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        RefreshTokenModel refreshToken = RefreshTokenModel.builder()
                .id(UUID.randomUUID().toString())
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .issuedAt(Instant.now())
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshTokenModel> validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(RefreshTokenModel::isValid);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(String userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));
        
        refreshTokenRepository.revokeAllUserTokens(user);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Run at midnight every day
    public void deleteAllExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now());
        log.info("Expired refresh tokens have been deleted");
    }

    @Override
    @Transactional
    public RefreshTokenModel rotateRefreshToken(String oldToken) {
        RefreshTokenModel refreshToken = refreshTokenRepository.findByToken(oldToken)
                .orElseThrow(() -> new FileOperationException("Invalid refresh token", "INVALID_REFRESH_TOKEN"));

        if (!refreshToken.isValid()) {
            throw new FileOperationException("Refresh token is expired or revoked", "INVALID_REFRESH_TOKEN");
        }

        // Revoke the old token
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // Create a new token for the same user
        return createRefreshToken(refreshToken.getUser().getId());
    }
}