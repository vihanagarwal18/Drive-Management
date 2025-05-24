package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.LoginDetails;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Dto.User;
import com.vihan.Drive.Management.Entity.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Maps a User DTO to a UserModel entity
     * 
     * @param user The User DTO to map
     * @return The mapped UserModel entity
     */
    @Mapping(source = "loginDetails.username", target = "username")
    UserModel toEntity(User user);

    /**
     * Maps a UserModel entity to a User DTO
     * 
     * @param userModel The UserModel entity to map
     * @return The mapped User DTO
     */
    @Mapping(source = "username", target = "loginDetails", qualifiedByName = "usernameToLoginDetails")
    User toDto(UserModel userModel);

    /**
     * Maps a RegisterRequest DTO to a UserModel entity
     * 
     * @param request The RegisterRequest DTO to map
     * @return The mapped UserModel entity
     */
    @Mapping(target = "id", expression = "java(generateUUID())")
    UserModel registerRequestToEntity(RegisterRequest request);

    /**
     * Generates a random UUID string
     * 
     * @return A random UUID string
     */
    default String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Creates a LoginDetails object from a username
     * 
     * @param username The username
     * @return A LoginDetails object with the username and null password
     */
    @Named("usernameToLoginDetails")
    default LoginDetails usernameToLoginDetails(String username) {
        return new LoginDetails(username, null);
    }
}