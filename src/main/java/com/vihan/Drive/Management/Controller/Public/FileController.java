package com.vihan.Drive.Management.Controller.Public;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Dto.RenameResponseDto;
import com.vihan.Drive.Management.Entity.FileModel;
import com.vihan.Drive.Management.Repository.FileRepository;
import com.vihan.Drive.Management.Repository.UserRepository;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/public/v1")
public class FileController {

    private final FileService fileService;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public FileController(FileService fileService, FileRepository fileRepository, UserRepository userRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

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

    @PostMapping(value = "/file/upload/{userId}")
    public File uploadFile(
                @PathVariable(value = "userId") String userId,
                @RequestParam("file") MultipartFile file,
                @RequestParam(value = "folderPath", defaultValue = "") String folderPath,
                @RequestParam(value = "displayName", required = false) String displayName,
                @RequestHeader(value = "fileType") FileType fileType,
                @RequestParam(value = "isPublic", defaultValue = "false") Boolean isPublic) {
            
            return fileService.uploadFile(file, userId, folderPath, displayName, fileType, isPublic);
    }

    @GetMapping(value = "/file/download/{userId}/{id}")
    public ResponseEntity<byte[]> downloadFile(
                @PathVariable(value = "id") String id,
                @PathVariable(value = "userId") String userId) {
            
            byte[] fileData = fileService.downloadFile(id, userId);
            FileModel fileModel = fileRepository.findByIdAndUser(id, userRepository.findById(userId).orElseThrow())
                    .orElseThrow(() -> new RuntimeException("File not found"));
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getDisplayName() + "\"")
                    .contentType(MediaType.parseMediaType(fileModel.getContentType() != null ? fileModel.getContentType() : "application/octet-stream"))
                    .body(fileData);
    }

    @DeleteMapping(value = "/file/{userId}/{id}")
    public ResponseEntity<Boolean> deleteFile(
                @PathVariable(value = "id") String id,
                @PathVariable(value = "userId") String userId) {
            
            boolean deleted = fileService.deleteFile(id, userId);
            return ResponseEntity.ok(deleted);
    }

    @GetMapping(value = "/files/{userId}")
    public List<File> listFiles(
                @PathVariable(value = "userId") String userId,
                @RequestParam(value = "folderPath", required = false) String folderPath) {
            
            if (folderPath != null) {
                return fileService.listFilesByUserAndFolder(userId, folderPath);
            } else {
                return fileService.listFilesByUser(userId);
            }
    }
}
