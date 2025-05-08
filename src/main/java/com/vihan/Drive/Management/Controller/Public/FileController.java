package com.vihan.Drive.Management.Controller.Public;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import com.vihan.Drive.Management.Service.Interface.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/public/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FileController {

    private final FileService fileService;

    @GetMapping(value = "/file/{id}")
    public File getFile(
            @PathVariable(value = "id") String id,
            @RequestParam(value = "displayName") String displayName,
            @RequestHeader(value = "fileType") FileType fileType) {
            return fileService.getFileById(id, displayName,fileType);
    }
}
