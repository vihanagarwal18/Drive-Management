package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;

public interface FileService {

    File getFile(String id, String userId);

    File getFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath);

    File createFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath);

    RenameResponseDto renameFile(String id, String userId, String displayName, String newName, FileType fileType,
            String internalPath, String externalPath);

    File uploadFile(MultipartFile file, String userId, String folderPath, String displayName,
                   FileType fileType, Boolean isPublic);

    byte[] downloadFile(String id, String userId);

    File generatePresignedUrl(String id, String userId, Duration expiration);

    boolean deleteFile(String id, String userId);

    List<File> listFilesByUser(String userId);

    List<File> listFilesByUserAndFolder(String userId, String folderPath);
}
