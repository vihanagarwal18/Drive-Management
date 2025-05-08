package com.vihan.Drive.Management.Service.Impl;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Override
    public File getFileById(String id, String userId,String displayName, FileType fileType,
            String internalPath, String externalPath)  {
        //make repository call to get file by id
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

    @Override
    public File createFile(String id, String userId,String displayName, FileType fileType,
            String internalPath, String externalPath)  {
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

    public RenameResponseDto renameFile(String id, String userId, String newName, FileType fileType,
                                        String internalPath, String externalPath){
        //make repository call to find file
        String dummyOldName = "dummyOldName"; //replace with actual old name
        //function to rename file
        //make repository call to save renamed file

        return RenameResponseDto.builder()
                .fileName(newName)
                .userName(userId)
                .oldName(dummyOldName)
                .newName(newName)
                .fileType(fileType)
                .internalPath(internalPath)
                .externalPath(externalPath)
                .build();
    }
}
