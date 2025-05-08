package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;

public interface FileService {

    File getFileById(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath);

    File createFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath);

    RenameResponseDto renameFile(String id, String userId, String newName, FileType fileType,
            String internalPath, String externalPath);
}
