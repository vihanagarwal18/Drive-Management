package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.User;
import com.vihan.Drive.Management.Entity.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "loginDetails", target = "loginDetails")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "encryptionKey", target = "encryptionKey")
    @Mapping(source = "decryptionKey", target = "decryptionKey")
    User userModelToUser(UserModel userModel);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "loginDetails", target = "loginDetails")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "encryptionKey", target = "encryptionKey")
    @Mapping(source = "decryptionKey", target = "decryptionKey")
    UserModel userToUserModel(User user);
} 