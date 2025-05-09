package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Entity.FileModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface FileMapper {
    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "fileType", target = "fileType")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "displayName", target = "displayName")
    @Mapping(source = "internalPath", target = "internalPath")
    @Mapping(source = "externalPath", target = "externalPath")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    File fileModelToFile(FileModel fileModel);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "fileType", target = "fileType")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "displayName", target = "displayName")
    @Mapping(source = "internalPath", target = "internalPath")
    @Mapping(source = "externalPath", target = "externalPath")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    FileModel fileToFileModel(File file);
} 