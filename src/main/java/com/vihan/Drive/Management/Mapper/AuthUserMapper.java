package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.AuthUser;
import com.vihan.Drive.Management.Entity.AuthUserModel;
import com.vihan.Drive.Management.Entity.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthUserMapper {

    AuthUserMapper INSTANCE = Mappers.getMapper(AuthUserMapper.class);

    @Mapping(source = "user", target = "user")
    AuthUserModel toEntity(AuthUser authUser);

    @Mapping(source = "user", target = "user")
    AuthUser toDto(AuthUserModel authUserModel);

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