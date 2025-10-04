package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Entity.RefreshTokenModel;

import java.util.Optional;

public interface RefreshTokenService {

    /**
     * Create a new refresh token for a user
     * @param userId The ID of the user
     * @return The created refresh token
     */
    RefreshTokenModel createRefreshToken(String userId);

    /**
     * Verify if a refresh token is valid
     * @param token The refresh token to verify
     * @return The refresh token if valid
     */
    Optional<RefreshTokenModel> validateRefreshToken(String token);

    /**
     * Delete a refresh token
     * @param token The refresh token to delete
     */
    void deleteRefreshToken(String token);

    /**
     * Revoke all refresh tokens for a user
     * @param userId The ID of the user
     */
    void revokeAllUserTokens(String userId);

    /**
     * Delete all expired tokens
     */
    void deleteAllExpiredTokens();

    /**
     * Rotate a refresh token (revoke the old one and create a new one)
     * @param oldToken The old refresh token
     * @return The new refresh token
     */
    RefreshTokenModel rotateRefreshToken(String oldToken);
}