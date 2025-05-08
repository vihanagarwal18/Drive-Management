package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.FileType;
import lombok.Builder;

@Builder
public record RenameResponseDto(

       String fileName,

       String userName,

       String oldName,

       String newName,

       FileType fileType,

       String internalPath,

       String externalPath
) {}
