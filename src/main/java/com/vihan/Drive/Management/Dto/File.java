package com.vihan.Drive.Management.Dto;

import com.vihan.Drive.Management.Constants.FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {

    private String id;

    private String name;

    private FileType fileType;

    private User user;

    private String displayName;

    private String internalPath;

    private String externalPath;
    
    private String s3Key;
    
    private String contentType;
    
    private Long size;
    
    private Boolean isPublic;
    
    private String presignedUrl;
}
