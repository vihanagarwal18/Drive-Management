package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Entity.FileModel;
import com.vihan.Drive.Management.Entity.UserModel;
import com.vihan.Drive.Management.Mapper.FileMapper;
import com.vihan.Drive.Management.Mapper.UserMapper;
import com.vihan.Drive.Management.Repository.FileRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final FileMapper fileMapper = FileMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    @Transactional(readOnly = true)
    public File getFile(String id, String userId, String displayName, FileType fileType,
                        String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        String userName = userModel.getLoginDetails().getUsername();

        FileModel fileModel = fileRepository.findFileByAttributes(id, userName, displayName, fileType, internalPath, externalPath);
        if (fileModel == null) {
            throw new IllegalArgumentException("File not found with given attributes");
        }
        
        return fileMapper.fileModelToFile(fileModel);
    }

    @Override
    @Transactional
    public File createFile(String id, String userId, String displayName, FileType fileType,
                           String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        String userName = userModel.getLoginDetails().getUsername();

        FileModel fileModel = FileModel.builder()
                .id(id)
                .name(displayName)
                .userName(userName)
                .displayName(displayName)
                .fileType(fileType)
                .user(userModel)
                .internalPath(internalPath)
                .externalPath(externalPath)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        fileModel = fileRepository.save(fileModel);
        return fileMapper.fileModelToFile(fileModel);
    }

    @Override
    @Transactional
    public RenameResponseDto renameFile(String id, String userId, String oldName, String newName, FileType fileType,
                                        String internalPath, String externalPath) {

        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        String userName = userModel.getLoginDetails().getUsername();

        FileModel fileModel = fileRepository.findFileByAttributes(id, userName, oldName, fileType, internalPath, externalPath);
        if (fileModel == null) {
            throw new IllegalArgumentException("File not found with given attributes");
        }

        fileModel.setDisplayName(newName);
        fileModel.setUpdatedAt(LocalDateTime.now());
        fileRepository.save(fileModel);

        return RenameResponseDto.builder()
                .fileName(newName)
                .userName(userName)
                .oldName(oldName)
                .newName(newName)
                .fileType(fileType)
                .internalPath(internalPath)
                .externalPath(externalPath)
                .build();
    }

    @Override
    @Transactional
    public void deleteFile(String id, String userId, String displayName, FileType fileType,
                          String internalPath, String externalPath) {
        UserModel userModel = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        String userName = userModel.getLoginDetails().getUsername();

        FileModel fileModel = fileRepository.findFileByAttributes(id, userName, displayName, fileType, internalPath, externalPath);
        if (fileModel == null) {
            throw new IllegalArgumentException("File not found with given attributes");
        }

        fileRepository.delete(fileModel);
    }
}