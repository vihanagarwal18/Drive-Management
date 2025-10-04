package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.LoginDetails;
import com.vihan.Drive.Management.Dto.RegisterRequest;
import com.vihan.Drive.Management.Dto.User;
import com.vihan.Drive.Management.Entity.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "loginDetails.username", target = "username")
    UserModel toEntity(User user);

    @Mapping(source = "username", target = "loginDetails", qualifiedByName = "usernameToLoginDetails")
    User toDto(UserModel userModel);

    @Mapping(target = "id", expression = "java(generateUUID())")
    UserModel registerRequestToEntity(RegisterRequest request);

    default String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Named("usernameToLoginDetails")
    default LoginDetails usernameToLoginDetails(String username) {
        return new LoginDetails(username, null);
    }
}