package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Dto.AuthUser;
import com.vihan.Drive.Management.Dto.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository {

    boolean checkDuplicateDecryptionKey(String decryptionKey);

    boolean checkDuplicateEncryptionKey(String encryptionKey);

    AuthUser getAuthUser(User user);

}
