package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUserModel, Long> {
    
    @Query("SELECT COUNT(a) > 0 FROM AuthUserModel a WHERE a.decryptionKey = :decryptionKey")
    boolean existsByDecryptionKey(@Param("decryptionKey") String decryptionKey);
    
    @Query("SELECT COUNT(a) > 0 FROM AuthUserModel a WHERE a.encryptionKey = :encryptionKey")
    boolean existsByEncryptionKey(@Param("encryptionKey") String encryptionKey);
    
    Optional<AuthUserModel> findByUser(UserModel user);
}
