package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.AuthUser;
import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AuthUserMapper {

    AuthUserMapper INSTANCE = Mappers.getMapper(AuthUserMapper.class);

    /**
     * Maps an AuthUser DTO to an AuthUserModel entity
     * 
     * @param authUser The AuthUser DTO to map
     * @return The mapped AuthUserModel entity
     */
    @Mapping(source = "user", target = "user")
    AuthUserModel toEntity(AuthUser authUser);

    /**
     * Maps an AuthUserModel entity to an AuthUser DTO
     * 
     * @param authUserModel The AuthUserModel entity to map
     * @return The mapped AuthUser DTO
     */
    @Mapping(source = "user", target = "user")
    AuthUser toDto(AuthUserModel authUserModel);

    /**
     * Creates an AuthUserModel entity with the given parameters
     * 
     * @param id The ID of the auth user
     * @param userModel The user model
     * @param encryptionKey The encryption key
     * @param decryptionKey The decryption key
     * @param encryptedPassword The encrypted password
     * @return The created AuthUserModel entity
     */
    default AuthUserModel createAuthUserModel(
            String id,
            UserModel userModel,
            String encryptionKey,
            String decryptionKey,
            String encryptedPassword) {
        
        return AuthUserModel.builder()
                .id(id)
                .user(userModel)
                .encryptionKey(encryptionKey)
                .decryptionKey(decryptionKey)
                .encryptedPassword(encryptedPassword)
                .build();
    }
}