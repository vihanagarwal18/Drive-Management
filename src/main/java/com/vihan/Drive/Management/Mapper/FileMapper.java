package com.vihan.Drive.Management.Mapper;

import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Entity.FileModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    /**
     * Maps a File DTO to a FileModel entity
     * 
     * @param file The File DTO to map
     * @return The mapped FileModel entity
     */
    @Mapping(source = "user", target = "user")
    FileModel toEntity(File file);

    /**
     * Maps a FileModel entity to a File DTO
     * 
     * @param fileModel The FileModel entity to map
     * @return The mapped File DTO
     */
    @Mapping(source = "user", target = "user")
    File toDto(FileModel fileModel);
}