package com.vihan.Drive.Management.Service.Interface;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;

public interface FileService {

    File getFileById(String id, String displayName, FileType fileType);
}
