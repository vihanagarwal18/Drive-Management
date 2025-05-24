package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Entity.FileModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "user", target = "user")
    FileModel toEntity(File file);

    @Mapping(source = "user", target = "user")
    File toDto(FileModel fileModel);
}