package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Dto.User;
import com.vihan.Drive.Management.Repository.FileRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Override
    public File getFile(String id, String userId,String displayName, FileType fileType,
            String internalPath, String externalPath)  {

        User user=userRepository.getUser(userId);
        String userName=user.getLoginDetails().username();

        return fileRepository.getFile(id, userName, displayName, fileType, internalPath, externalPath);
    }

    @Override
    public File createFile(String id, String userId,String displayName, FileType fileType,
            String internalPath, String externalPath)  {

        User user=userRepository.getUser(userId);
        String userName=user.getLoginDetails().username();

        File file=fileRepository.getFile(id, userName, displayName, fileType, internalPath, externalPath);
        //make repository call to create file
        //for now returning a dummy file
        //find user with user id
        return File.builder().
                id(id).
                displayName(displayName).
                fileType(fileType).
                internalPath(internalPath).
                externalPath(externalPath).
                build();
    }

    public RenameResponseDto renameFile(String id, String userId, String oldName, String newName, FileType fileType,
                                        String internalPath, String externalPath){

        User user=userRepository.getUser(userId);
        String userName=user.getLoginDetails().username();

        File file=fileRepository.getFile(id, userName, oldName, fileType, internalPath, externalPath);

        renameFile(file, newName);

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

    private void renameFile(File file, String newName) {
        file.setName(newName);
        //make repository call to save renamed file
    }

    private void deleteFile(File file) {
        //make repository call to soft  delete file
    }

    private void createFile(File file) {
        //make repository call to create file
    }
}

