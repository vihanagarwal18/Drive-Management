package com.vihan.Drive.Management.Repository;

import com.vihan.Drive.Management.Constants.FileType;
import com.vihan.Drive.Management.Dto.File;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository {

    File getFile(String id, String userName, String displayName, FileType fileType,
                 String internalPath, String externalPath);
}
