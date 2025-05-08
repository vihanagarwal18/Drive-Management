package com.vihan.Drive.Management.Controller.Public;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/public/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/file/{userId}/{id}")
    public File getFile(
                @PathVariable(value = "id") String id,
                @PathVariable(value = "userId") String userId,
                @RequestParam(value = "displayName") String displayName,
                @RequestHeader(value = "fileType") FileType fileType,
                @RequestHeader(value = "internalPath") String internalPath,
                @RequestHeader(value = "externalPath") String externalPath) {

            return fileService.getFile(id, userId, displayName, fileType, internalPath, externalPath);
    }

    @PostMapping(value = "/file/{userId}/{id}")
    public File createFile(
                @PathVariable(value = "id") String id,
                @PathVariable(value = "userId") String userId,
                @RequestParam(value = "displayName") String displayName,
                @RequestHeader(value = "fileType") FileType fileType,
                @RequestHeader(value = "internalPath") String internalPath,
                @RequestHeader(value = "externalPath") String externalPath) {

            return fileService.createFile(id, userId, displayName, fileType, internalPath, externalPath);
    }

    @PutMapping(value = "/file/rename/{userId}/{id}")
    public RenameResponseDto renameFile(
                @PathVariable(value = "id") String id,
                @PathVariable(value = "userId") String userId,
                @RequestParam(value = "oldName") String oldName,
                @RequestParam(value = "newName") String newName,
                @RequestHeader(value = "fileType") FileType fileType,
                @RequestHeader(value = "internalPath") String internalPath,
                @RequestHeader(value = "externalPath") String externalPath) {

            return fileService.renameFile(id, userId, oldName, newName, fileType, internalPath, externalPath);
    }
}
