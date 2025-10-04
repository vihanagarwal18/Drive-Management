package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Entity.RefreshTokenModel;
import com.vihan.Drive.Management.Entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenModel, String> {

    Optional<RefreshTokenModel> findByToken(String token);

    List<RefreshTokenModel> findByUser(UserModel user);

    void deleteByUser(UserModel user);

    @Modifying
    @Query("UPDATE RefreshTokenModel r SET r.revoked = true WHERE r.user = :user")
    void revokeAllUserTokens(@Param("user") UserModel user);

    @Modifying
    @Query("DELETE FROM RefreshTokenModel r WHERE r.expiryDate < :now")
    void deleteAllExpiredTokens(@Param("now") Instant now);

    @Modifying
    @Query("UPDATE RefreshTokenModel r SET r.revoked = true WHERE r.id = :id")
    void revokeToken(@Param("id") String id);

    boolean existsByTokenAndRevokedFalseAndExpiryDateAfter(String token, Instant now);
}