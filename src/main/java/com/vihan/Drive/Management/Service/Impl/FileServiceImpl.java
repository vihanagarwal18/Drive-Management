package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Entity.FileModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Exception.FileOperationException;
import com.vihan.Drive.Management.Mapper.FileMapper;
import com.vihan.Drive.Management.Repository.FileRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.FileService;
import com.vihan.Drive.Management.Service.Interface.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Override
    @Transactional(readOnly = true)
    public File getFile(String id, String userId) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        FileModel fileModel = fileRepository.findByIdAndUser(id, userModel)
                .orElseThrow(() -> new FileOperationException("File not found", "FILE_NOT_FOUND"));

        return FileMapper.INSTANCE.toDto(fileModel);
    }

    @Override
    @Transactional(readOnly = true)
    public File getFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        FileModel fileModel = fileRepository.findByAllParameters(id, userModel, displayName, fileType, internalPath, externalPath)
                .orElseThrow(() -> new FileOperationException("File not found", "FILE_NOT_FOUND"));

        return FileMapper.INSTANCE.toDto(fileModel);
    }

    @Override
    @Transactional
    public File createFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        FileModel fileModel = FileModel.builder()
                .id(id != null ? id : UUID.randomUUID().toString())
                .name(displayName)
                .displayName(displayName)
                .fileType(fileType)
                .user(userModel)
                .internalPath(internalPath)
                .externalPath(externalPath)
                .isPublic(false)
                .build();

        fileModel = fileRepository.save(fileModel);

        return FileMapper.INSTANCE.toDto(fileModel);
    }

    @Override
    @Transactional
    public RenameResponseDto renameFile(String id, String userId, String oldName, String newName, FileType fileType,
            String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        FileModel fileModel = fileRepository.findByIdAndUserAndDisplayName(id, userModel, oldName)
                .orElseThrow(() -> new FileOperationException("File not found", "FILE_NOT_FOUND"));

        fileModel.setName(newName);
        fileModel.setDisplayName(newName);

        fileRepository.save(fileModel);

        return RenameResponseDto.builder()
                .fileName(newName)
                .userName(userId)
                .oldName(oldName)
                .newName(newName)
                .fileType(fileType)
                .internalPath(internalPath)
                .externalPath(externalPath)
                .build();
    }

    @Override
    @Transactional
    public File uploadFile(MultipartFile file, String userId, String folderPath, String displayName,
                          FileType fileType, Boolean isPublic) {
        try {
            UserModel userModel = userRepository.findById(userId)
                    .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

            String s3Key = s3Service.uploadFile(file, userId, folderPath);

            FileModel fileModel = FileModel.builder()
                    .id(UUID.randomUUID().toString())
                    .name(file.getOriginalFilename())
                    .displayName(displayName != null ? displayName : file.getOriginalFilename())
                    .fileType(fileType)
                    .user(userModel)
                    .internalPath(folderPath)
                    .s3Key(s3Key)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .isPublic(isPublic != null ? isPublic : false)
                    .build();

            fileModel = fileRepository.save(fileModel);

            return FileMapper.INSTANCE.toDto(fileModel);
        } catch (Exception e) {
            log.error("Error uploading file", e);
            throw new FileOperationException("Failed to upload file", "UPLOAD_FAILED", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadFile(String id, String userId) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        FileModel fileModel = fileRepository.findByIdAndUser(id, userModel)
                .orElseThrow(() -> new FileOperationException("File not found", "FILE_NOT_FOUND"));

        if (fileModel.getS3Key() == null) {
            throw new FileOperationException("File not found in S3", "S3_FILE_NOT_FOUND");
        }

        return s3Service.downloadFile(fileModel.getS3Key());
    }

    @Override
    @Transactional(readOnly = true)
    public File generatePresignedUrl(String id, String userId, Duration expiration) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        FileModel fileModel = fileRepository.findByIdAndUser(id, userModel)
                .orElseThrow(() -> new FileOperationException("File not found", "FILE_NOT_FOUND"));

        if (fileModel.getS3Key() == null) {
            throw new FileOperationException("File not found in S3", "S3_FILE_NOT_FOUND");
        }

        String presignedUrl = s3Service.generatePresignedUrl(fileModel.getS3Key(), expiration);

        File fileDto = FileMapper.INSTANCE.toDto(fileModel);
        fileDto.setPresignedUrl(presignedUrl);

        return fileDto;
    }

    @Override
    @Transactional
    public boolean deleteFile(String id, String userId) {
        try {
            UserModel userModel = userRepository.findById(userId)
                    .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

            FileModel fileModel = fileRepository.findByIdAndUser(id, userModel)
                    .orElseThrow(() -> new FileOperationException("File not found", "FILE_NOT_FOUND"));

            if (fileModel.getS3Key() != null) {
                s3Service.deleteFile(fileModel.getS3Key());
            }

            fileRepository.delete(fileModel);

            return true;
        } catch (FileOperationException e) {
            log.error("Error deleting file", e);
            throw e;
        } catch (Exception e) {
            log.error("Error deleting file", e);
            throw new FileOperationException("Failed to delete file", "DELETE_FAILED", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<File> listFilesByUser(String userId) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        List<FileModel> fileModels = fileRepository.findByUser(userModel);

        return fileModels.stream()
                .map(FileMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<File> listFilesByUserAndFolder(String userId, String folderPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new FileOperationException("User not found with id: " + userId, "USER_NOT_FOUND"));

        List<FileModel> fileModels = fileRepository.findByUserAndInternalPath(userModel, folderPath);

        return fileModels.stream()
                .map(FileMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
}
