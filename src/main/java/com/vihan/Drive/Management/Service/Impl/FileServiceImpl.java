package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Entity.FileModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Mapper.FileMapper;
import com.vihan.Drive.Management.Repository.FileRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileMapper fileMapper;

    @Override
    @Transactional(readOnly = true)
    public File getFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        FileModel fileModel = fileRepository.findByAllParameters(id, userModel, displayName, fileType, internalPath, externalPath)
                .orElseThrow(() -> new RuntimeException("File not found"));

        return fileMapper.toDto(fileModel);
    }

    @Override
    @Transactional
    public File createFile(String id, String userId, String displayName, FileType fileType,
            String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        FileModel fileModel = FileModel.builder()
                .id(id != null ? id : UUID.randomUUID().toString())
                .name(displayName)
                .displayName(displayName)
                .fileType(fileType)
                .user(userModel)
                .internalPath(internalPath)
                .externalPath(externalPath)
                .build();

        fileModel = fileRepository.save(fileModel);

        return fileMapper.toDto(fileModel);
    }

    @Override
    @Transactional
    public RenameResponseDto renameFile(String id, String userId, String oldName, String newName, FileType fileType,
            String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        FileModel fileModel = fileRepository.findByIdAndUserAndDisplayName(id, userModel, oldName)
                .orElseThrow(() -> new RuntimeException("File not found"));

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

    @Transactional
    public boolean deleteFile(String id, String userId) {
        try {

            UserModel userModel = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            FileModel fileModel = fileRepository.findByIdAndUser(id, userModel)
                    .orElseThrow(() -> new RuntimeException("File not found"));

            // Delete file (or implement soft delete logic here)
            fileRepository.delete(fileModel);

            return true;
        } catch (Exception e) {
            log.error("Error deleting file", e);
            return false;
        }
    }
}
