package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.AuthUser;
import com.vihan.Drive.Management.Entity.AuthUserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface AuthUserMapper {
    AuthUserMapper INSTANCE = Mappers.getMapper(AuthUserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "encryptionKey", target = "encryptionKey")
    @Mapping(source = "decryptionKey", target = "decryptionKey")
    @Mapping(source = "encryptedPassword", target = "encryptedPassword")
    AuthUser authUserModelToAuthUser(AuthUserModel authUserModel);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "encryptionKey", target = "encryptionKey")
    @Mapping(source = "decryptionKey", target = "decryptionKey")
    @Mapping(source = "encryptedPassword", target = "encryptedPassword")
    AuthUserModel authUserToAuthUserModel(AuthUser authUser);
} 