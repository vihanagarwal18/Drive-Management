package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<AuthUserModel, String> {


    Optional<AuthUserModel> findByUser(UserModel user);

    @Query("SELECT a FROM AuthUserModel a WHERE a.user.id = :userId")
    Optional<AuthUserModel> findByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(a) = 0 FROM AuthUserModel a WHERE a.encryptionKey = :encryptionKey")
    boolean checkDuplicateEncryptionKey(@Param("encryptionKey") String encryptionKey);

    @Query("SELECT COUNT(a) = 0 FROM AuthUserModel a WHERE a.decryptionKey = :decryptionKey")
    boolean checkDuplicateDecryptionKey(@Param("decryptionKey") String decryptionKey);
}
